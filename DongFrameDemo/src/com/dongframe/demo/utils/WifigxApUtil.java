package com.dongframe.demo.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.List;

import com.dongframe.demo.DongApplication;
import com.dongframe.demo.R;
import com.dongframe.demo.infos.Software;
import com.dongframe.demo.upgrade.UpgradeService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by TXWL on 2015/10/29.
 */
public class WifigxApUtil
{
    private static final String TAG = WifigxApUtil.class.getSimpleName();
    
    private static String DEVICE_ID = null;
    
    public static float screenDensity = -1;
    
    private static int displayWidth = 0;
    
    private static int displayHeight = 0;
    
    private static int statusBarHeight = 0;
    
    // 获取屏幕宽度
    public static int getDisplayWidth(Context context)
    {
        if (displayWidth <= 0)
        {
            WindowManager wm = (WindowManager)context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            displayWidth = dm.widthPixels;
        }
        return displayWidth;
    }
    
    // 获取屏幕高度
    public static int getDisplayHeight(Context context)
    {
        if (displayHeight <= 0)
        {
            WindowManager wm = (WindowManager)context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            displayHeight = dm.heightPixels;
        }
        return displayHeight;
    }
    
    /**
     * 获得状态栏的高
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context)
    {
        if (statusBarHeight <= 0)
        {
            try
            {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(x);
            }
            catch (Exception e)
            {
                LogUtils.LOGE(TAG, "get status bar height fail");
            }
        }
        return statusBarHeight;
    }
    
    /**
     * 获得屏幕密度
     *
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context)
    {
        if (screenDensity <= 0)
        {
            WindowManager wm = (WindowManager)context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            screenDensity = dm.density;
        }
        SharedUtil.storeScreenDensity(context, screenDensity);
        return screenDensity;
    }
    
    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context)
    {
        String versionName = "";
        try
        {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0)
            {
                return "";
            }
        }
        catch (Exception e)
        {
            LogUtils.LOGE("VersionInfo", "Exception", e);
        }
        return versionName;
    }
    
    /**
     * 返回当前程序版本号
     */
    public static int getAppVersionCode(Context context)
    {
        int versionCode = 0;
        try
        {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        }
        catch (Exception e)
        {
            LogUtils.LOGE("VersionInfo", "Exception", e);
        }
        return versionCode;
    }
    
    /**
     * 获得手机卡的IMSI
     *
     * @param context
     * @return
     */
    public static String getSubscriberId(Context context)
    {
        String subscriberId = "";
        
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        try
        {
            if (tm != null)
            {
                Object obj = invokeDeclaredMethod(tm.getClass(), tm, "getSubscriberId", null, null);
                if (obj != null && obj instanceof String)
                {
                    subscriberId = String.valueOf(obj);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (TextUtils.isEmpty(subscriberId))
            {
                if (tm != null)
                {
                    subscriberId = tm.getSubscriberId();
                }
            }
        }
        
        if (TextUtils.isEmpty(subscriberId))
        {
            LogUtils.LOGV(TAG, "Failed to get IMSI!");
        }
        
        return subscriberId;
    }
    
    // Added by Shawn 2012-07-31 判断是否有sim卡
    public static boolean ifHasSim(Context context)
    {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String sn = tm.getSubscriberId();
        if (sn != null && sn.length() > 0)
        {
            return true;
        }
        else
        {
            if (tm.getSimState() == TelephonyManager.SIM_STATE_READY)
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获得自定义设备ID
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context)
    {
        if (DEVICE_ID == null || DEVICE_ID.length() > 0)
        {
            DeviceUuidFactory factory = new DeviceUuidFactory(context);
            DEVICE_ID = factory.getDeviceUuid();
        }
        return DEVICE_ID;
    }
    
    /**
     * 获得设备的IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEIId(Context context)
    {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }
    
    public static Object invokeDeclaredMethod(Class<?> declaredClass, Object classObject, String methodName,
        Object params[], Class<?> paramTypes[])
    {
        Object ret = null;
        try
        {
            Method method = declaredClass.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            if (params == null)
            {
                ret = method.invoke(classObject, new Object[0]);
            }
            else
            {
                ret = method.invoke(classObject, params);
            }
        }
        catch (Exception exception)
        {
            LogUtils.LOGV(TAG, "Exception in invokeDeclaredMethod", exception);
        }
        return ret;
    }
    
    /**
     * 获取手机厂商
     *
     * @return
     */
    public static String getManufacturer()
    {
        return android.os.Build.MANUFACTURER;
    }
    
    /**
     * 获取手机类型
     *
     * @return
     */
    public static String getType()
    {
        return android.os.Build.MODEL;
    }
    
    public static String getOSVer()
    {
        return String.valueOf(android.os.Build.VERSION.SDK_INT);
    }
    
    public static String getVersion()
    {
        return android.os.Build.VERSION.RELEASE;
    }
    
    /**
     * 获得渠道ID
     *
     * @return
     */
    public static String getChannelID()
    {
        return DongApplication.getInstance().getChannelId();
    }
    
    /**
     * 检测网络是否可用
     *
     * @return true 可用，false不可用
     */
    public static boolean isNetConnected(Context context)
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnected();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 获得wlan mac 地址
     *
     * @param context
     * @return
     */
    public static String getWlanMac(Context context)
    {
        // 获取mac地址：
        String macAddress = "00-00-00-00-00-00";
        try
        {
            WifiManager wifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            if (null != info && !TextUtils.isEmpty(info.getMacAddress()))
            {
                macAddress = info.getMacAddress().replace(":", "-").toUpperCase();
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return macAddress;
    }
    
    // 获取手机的mac地址
    @SuppressLint("NewApi")
    public static String getLocalMacAddress(Context context)
    {
        try
        {
            for (Enumeration e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements();)
            {
                NetworkInterface item = (NetworkInterface)e.nextElement();
                byte[] mac = item.getHardwareAddress();
                if (mac != null && mac.length > 0)
                {
                    // 下面代码是把mac地址拼装成String
                    StringBuffer sb = new StringBuffer();
                    
                    for (int i = 0; i < mac.length; i++)
                    {
                        if (i != 0)
                        {
                            sb.append("-");
                        }
                        // mac[i] & 0xFF 是为了把byte转化为正整数
                        String s = Integer.toHexString(mac[i] & 0xFF);
                        sb.append(s.length() == 1 ? 0 + s : s);
                    }
                    
                    // 把字符串所有小写字母改为大写成为正规的mac地址并返回
                    return sb.toString().toUpperCase();
                }
            }
        }
        catch (Exception e)
        {
        }
        return "";
    }
    
    /**
     * 检测WIFI网络是否可用
     *
     * @return true 可用，false不可用
     */
    public static boolean isWIFIConnected(Context context)
    {
        boolean isWIFIConnected = false;
        
        try
        {
            ConnectivityManager connManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (null != networkInfo && networkInfo.isConnected()
                && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI))
            {
                isWIFIConnected = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return isWIFIConnected;
    }
    
    /**
     * 检测网络数据是否可用
     *
     * @return true 可用，false不可用
     */
    public static boolean isMobileConnected(Context context)
    {
        boolean isMobileConnected = false;
        
        try
        {
            ConnectivityManager connManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (null != networkInfo && networkInfo.isConnected()
                && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE))
            {
                isMobileConnected = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return isMobileConnected;
    }
    
    /** 判断GPS是否打开
     * <功能详细描述>
     * @param context
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isGPSAvailable(Context context)
    {
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    
    /**
     * 判断是否最新版本
     *
     * @param context
     * @param software
     * @return
     */
    public static boolean isInitVersion(Context context, Software software)
    {
        if (null == software)
        {
            return false;
        }
        Software oldSoft = SharedUtil.getSoftUpdate(context);
        int newsCode = software.getVersionCode();
        int oldCode = oldSoft.getVersionCode();
        int curCode = getAppVersionCode(context);
        if (newsCode > oldCode && newsCode > curCode && newsCode > 1)
        {
            FileUtil.deleteApkFile(context);
            SharedUtil.setUpgradeFileDownloaded(context, false);
            SharedUtil.setSoftUpdate(context, software);
            return true;
        }
        if (newsCode == oldCode && newsCode > curCode)
        {
            return true;
        }
        return false;
    }
    
    public static void startDownLoadApkServer(Context context)
    {
        Intent intent = new Intent(UpgradeService.SERVICE_DOWNLOAD_APK);
        intent.putExtra(UpgradeService.DOWNLOAD_HIDE_KEY, false);
        intent.setClass(context, UpgradeService.class);
        context.startService(intent);
    }
    
    /**
     * 为我们评分
     */
    public static void asWeScore(Context context)
    {
        String str = "market://details?id=" + context.getPackageName();
        PackageManager mPackageManager = context.getPackageManager();
        Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        List<ResolveInfo> resolveInfoList =
            mPackageManager.queryIntentActivities(localIntent, PackageManager.GET_INTENT_FILTERS);
        if (null != resolveInfoList && resolveInfoList.size() > 0)
        {
            // String str = "market://details?id=" + context.getPackageName();
            // Intent localIntent = new Intent("android.intent.action.VIEW");
            // localIntent.setData(Uri.parse(str));
            context.startActivity(localIntent);
        }
        else
        {
            Toast.makeText(context, R.string.aswescore_no_apply, Toast.LENGTH_SHORT).show();
        }
    }
    
    public static String getMD5(String info)
    {
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++)
            {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1)
                {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                }
                else
                {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            return "";
        }
        catch (UnsupportedEncodingException e)
        {
            return "";
        }
    }
}
