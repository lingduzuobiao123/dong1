package com.dongframe.demo;

import java.util.Map;

import com.dongframe.demo.utils.CrashHandler;
import com.dongframe.demo.utils.LogUtils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class DongApplication extends Application
{
    private final static String TAG = DongApplication.class.getSimpleName();
    
    /**
     * 用户权限(用户权限为一个功能ID数组，需保存客户端，然后在APP界面显示的时候每一个按钮都有特定的一个ID，与此数组比对，符合则显示按钮)
     */
    private Map<String, String> rightMap;
    
    private String channelID;// 渠道ID
    
    private static DongApplication mInstance = null;
    
    public static DongApplication getInstance()
    {
        return mInstance;
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
        
        if (LogUtils.DEBUG)
        {
            CrashHandler crashHandler = CrashHandler.getInstance();
            // 注册crashHandler
            crashHandler.init(getApplicationContext());
            Thread.setDefaultUncaughtExceptionHandler(crashHandler);
        }
    }
    
    /**
     * 获取渠道ID
     * 
     * @return
     */
    public String getChannelId()
    {
        if (channelID == null || channelID.equals("") || channelID.equals("null"))
        {
            try
            {
                ApplicationInfo appInfo =
                    this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                Object channel = appInfo.metaData.get("UMENG_CHANNEL");
                channelID = String.valueOf(channel);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        LogUtils.LOGI(TAG, "==getChannelId==" + channelID);
        return channelID;
    }
    
    public Map<String, String> getRightMap()
    {
        return rightMap;
    }
    
    public void setRightMap(Map<String, String> rightMap)
    {
        this.rightMap = rightMap;
    }
    
    public boolean isContainsRight(String key)
    {
        return null == rightMap ? false : rightMap.containsKey(key);
    }
    
}
