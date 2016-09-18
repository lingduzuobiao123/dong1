package com.dongframe.demo;

import com.dongframe.demo.utils.CrashHandler;
import com.dongframe.demo.utils.LogUtils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class DongApplication extends Application
{
    private final static String TAG = DongApplication.class.getSimpleName();
    
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
    
}
