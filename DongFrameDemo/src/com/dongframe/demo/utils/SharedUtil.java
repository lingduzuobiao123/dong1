package com.dongframe.demo.utils;

import com.dongframe.demo.infos.Software;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedUtil
{
    private static final String TAG = SharedUtil.class.getSimpleName();
    
    public final static String SP_NAME_MAIN = "SP_NAME_MAIN";
    
    public static final String MAIN_MORE_SOFTUPDATE_DOWNLOADED = "MAIN_MORE_SOFTUPDATE_DOWNLOADED";
    
    public final static String SP_SCREENDENSITY_KEY = "SP_SCREENDENSITY_KEY";
    
    // 版本更新
    private static final String MAIN_MORE_SOFTUPDATE_TITLE = "software_title";
    
    private static final String MAIN_MORE_SOFTUPDATE_PACK = "software_pack";
    
    private static final String MAIN_MORE_SOFTUPDATE_INFO = "software_info";
    
    private static final String MAIN_MORE_SOFTUPDATE_VERSION = "software_versionName";
    
    private static final String MAIN_MORE_SOFTUPDATE_CODE = "software_versionCode";
    
    private static final String MAIN_MORE_SOFTUPDATE_APK_URL = "software_apkUrl";
    
    private static final String MAIN_MORE_SOFTUPDATE_APK_SIZE = "software_apkSize";
    
    private static final String MAIN_MORE_SOFTUPDATE_FORCE_UPDATE = "software_force_update";
    
    private static final String CREATE_INSTALL_SHORTCUT = "create_install_shortcut";
    
    private static final String SHOWGUIDE = "show_guide_1";// 是否显示引导
    
    private static final String LOGIN_MEMORY_PASS = "login_memory_pass";// 是否记住密码
    
    private static final String LOGIN_USER_NAME = "login_user_name";// 存储用户名
    
    private static final String LOGIN_USER_PASS = "login_user_pass";// 存储用户密码
    
    /**
     * 保存屏幕密度到share，以便widget里面使用
     */
    public static void storeScreenDensity(Context context, float density)
    {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putFloat(SP_SCREENDENSITY_KEY, density);
        editor.commit();
        
    }
    
    /**
     * 从share获取屏幕密度
     */
    public static float fetchScreenDensity(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, Context.MODE_PRIVATE);
        return sp != null ? sp.getFloat(SP_SCREENDENSITY_KEY, 1) : 1;
    }
    
    /**
     * 下载状态
     * 
     * @param context
     * @param isDownloaded
     *            true下载完
     */
    public static void setUpgradeFileDownloaded(Context context, boolean isDownloaded)
    {
        LogUtils.LOGE(TAG, "==setUpgradeFileDownloaded===" + isDownloaded);
        try
        {
            SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
            Editor editor = sp.edit();
            editor.putBoolean(MAIN_MORE_SOFTUPDATE_DOWNLOADED, isDownloaded);
            editor.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 是否下载完
     * 
     * @param context
     * @return true，下载完
     */
    public static boolean isUpgradeFileDownloaded(Context context)
    {
        boolean isDownloaded = false;
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
        isDownloaded = sp.getBoolean(MAIN_MORE_SOFTUPDATE_DOWNLOADED, false);
        return isDownloaded;
    }
    
    // 获取更新信息
    public static Software getSoftUpdate(Context context)
    {
        
        Software software = new Software();
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
        software.setTitle(sp.getString(MAIN_MORE_SOFTUPDATE_TITLE, ""));
        software.setPackName(sp.getString(MAIN_MORE_SOFTUPDATE_PACK, ""));
        software.setInfo(sp.getString(MAIN_MORE_SOFTUPDATE_INFO, ""));
        software.setVersionName(sp.getString(MAIN_MORE_SOFTUPDATE_VERSION, ""));
        software.setVersionCode(sp.getInt(MAIN_MORE_SOFTUPDATE_CODE, 0));
        software.setUpdateUrl(sp.getString(MAIN_MORE_SOFTUPDATE_APK_URL, ""));
        software.setApkSize(sp.getLong(MAIN_MORE_SOFTUPDATE_APK_SIZE, 0));
        software.setForceUpd(sp.getInt(MAIN_MORE_SOFTUPDATE_FORCE_UPDATE, 0));
        return software;
        
    }
    
    // 存储更新信息
    public static void setSoftUpdate(Context context, Software software)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
            Editor editor = sp.edit();
            if (software != null)
            {
                editor.putString(MAIN_MORE_SOFTUPDATE_TITLE, software.getTitle());
                editor.putString(MAIN_MORE_SOFTUPDATE_PACK, software.getPackName());
                editor.putString(MAIN_MORE_SOFTUPDATE_INFO, software.getInfo());
                editor.putString(MAIN_MORE_SOFTUPDATE_VERSION, software.getVersionName());
                editor.putInt(MAIN_MORE_SOFTUPDATE_CODE, software.getVersionCode());
                editor.putString(MAIN_MORE_SOFTUPDATE_APK_URL, software.getUpdateUrl());
                editor.putLong(MAIN_MORE_SOFTUPDATE_APK_SIZE, software.getApkSize());
                editor.putInt(MAIN_MORE_SOFTUPDATE_FORCE_UPDATE, software.getForceUpd());
            }
            editor.commit();
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    
    // 存储apk的大小
    public static void setSoftUpdateApkSize(Context context, long apkSize)
    {
        LogUtils.LOGI(TAG, "===setSoftUpdateApkSize===" + apkSize);
        try
        {
            SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
            Editor editor = sp.edit();
            editor.putLong(MAIN_MORE_SOFTUPDATE_APK_SIZE, apkSize);
            editor.commit();
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    
    /**
     * 设为创建快捷方式
     * 
     * @param context
     */
    public static void setCreateShortcut(Context context)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
            Editor editor = sp.edit();
            editor.putBoolean(CREATE_INSTALL_SHORTCUT, true);
            editor.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 是否创建快捷方式
     * 
     * @param context
     * @return
     */
    public static boolean isCreateShortcut(Context context)
    {
        boolean isChecked = false;
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
        isChecked = sp.getBoolean(CREATE_INSTALL_SHORTCUT, false);
        return isChecked;
    }
    
    /**
     * 是否显示引导
     * 
     * @param context
     * @return true显示，false不显示
     */
    public static boolean isShowGuide(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
        return sp.getBoolean(SHOWGUIDE, true);
    }
    
    /**
     * 设置是否显示引导
     * 
     * @param context
     */
    public static void setShowGuide(Context context)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
            Editor editor = sp.edit();
            editor.putBoolean(SHOWGUIDE, false);
            editor.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取用户名
     * @param context
     */
    public static String getLoginName(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
        return sp.getString(LOGIN_USER_NAME, "");
    }
    
    /**
     * 设置用户名
     * 
     * @param context
     */
    public static void setLoginName(Context context, String userName)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
            Editor editor = sp.edit();
            editor.putString(LOGIN_USER_NAME, userName);
            editor.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 是否记住密码
     * 
     * @param context
     * @return true记住
     */
    public static boolean isMemoryPass(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
        return sp.getBoolean(LOGIN_MEMORY_PASS, false);
    }
    
    /**
     * 设置是否记住密码
     * 
     * @param context
     */
    public static void setMemoryPass(Context context, boolean isPass)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
            Editor editor = sp.edit();
            editor.putBoolean(LOGIN_MEMORY_PASS, isPass);
            editor.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取登录密码
     * 
     * @param context
     */
    public static String getLoginPass(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
        return sp.getString(LOGIN_USER_PASS, "");
    }
    
    /**
     * 设置登录密码
     * 
     * @param context
     */
    public static void setLoginPass(Context context, String password)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences(SP_NAME_MAIN, 0);
            Editor editor = sp.edit();
            editor.putString(LOGIN_USER_PASS, password);
            editor.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
