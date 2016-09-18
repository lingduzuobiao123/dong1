package com.dongframe.demo.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class LogUtils
{
    public static boolean DEBUG = true;
    
    /*
     * private static final String LOG_PREFIX = "SurfNews_"; private static
     * final int LOG_PREFIX_LENGTH = LOG_PREFIX.length(); private static final
     * int MAX_LOG_TAG_LENGTH = 23;
     * 
     * public static String makeLogTag(String str) { if (str.length() >
     * MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) { return LOG_PREFIX +
     * str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1); }
     * 
     * return LOG_PREFIX + str; }
     */
    
    /**
     * WARNING: Don't use this when obfuscating class names with Proguard!
     */
    /*
     * public static String makeLogTag(Class cls) { return
     * makeLogTag(cls.getSimpleName()); }
     */
    
    public static void LOGD(final String tag, String message)
    {
        // if (Log.isLoggable(tag, Log.DEBUG)) {
        if (DEBUG)
        {
            Log.d(tag, message);
        }
    }
    
    public static void LOGD(final String tag, String message, Throwable cause)
    {
        // if (Log.isLoggable(tag, Log.DEBUG)) {
        if (DEBUG)
        {
            Log.d(tag, message, cause);
        }
    }
    
    public static void LOGV(final String tag, String message)
    {
        // noinspection PointlessBooleanExpression,ConstantConditions
        // if (DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
        if (DEBUG)
        {
            Log.v(tag, message);
        }
    }
    
    public static void LOGV(final String tag, String message, Throwable cause)
    {
        // noinspection PointlessBooleanExpression,ConstantConditions
        // if (DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
        if (DEBUG)
        {
            Log.v(tag, message, cause);
        }
    }
    
    public static void LOGI(final String tag, String message)
    {
        if (DEBUG)
        {
            Log.i(tag, message);
        }
    }
    
    public static void LOGI(final String tag, String message, Throwable cause)
    {
        if (DEBUG)
        {
            Log.i(tag, message, cause);
        }
    }
    
    public static void LOGW(final String tag, String message)
    {
        if (DEBUG)
        {
            Log.w(tag, message);
        }
    }
    
    public static void LOGW(final String tag, String message, Throwable cause)
    {
        if (DEBUG)
        {
            Log.w(tag, message, cause);
        }
    }
    
    public static void LOGE(final String tag, String message)
    {
        if (DEBUG)
        {
            Log.e(tag, message);
        }
    }
    
    public static void LOGE(final String tag, String message, Throwable cause)
    {
        if (DEBUG)
        {
            Log.e(tag, message, cause);
        }
    }
    
    private LogUtils()
    {
    }
    
    private static String MYLOGFILEName = "txwlkj_wifiTx_Log.txt";// 本类输出的日志文件名称
    
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");// 日志的输出格式 发现是在android中，创建文件时，文件名中不能包含“：”冒号 java.io.IOException: open failed: EINVAL (Invalid argument)
    
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
    
    public static final String LOG_FILE_PATH = "/dongframe/log/";
    
    private static final String LOG_CRUSH_PATH = "/dongframe/com.dongframe.demo/";
    
    public static void putLog(Context context, String message)
    {
        if (DEBUG)
        {
            Log.e("LogUtils====putLog", message);
            writeLogtoFile(context, "e", "txwlkj", message);
        }
    }
    
    /**
     * 打开日志文件并写入日志
     * 
     * @return
     * **/
    private static void writeLogtoFile(Context context, String mylogtype, String tag, String text)
    {// 新建或打开日志文件
        Date nowtime = new Date();
        String needWriteFiel = logfile.format(nowtime);
        String needWriteMessage =
            myLogSdf.format(nowtime) + "    ==" + System.currentTimeMillis() + "==    " + tag + "    " + text;
        String filepath = null;
        
        // if (android.os.Build.VERSION.SDK_INT < 8) {
        // if (FileUtil.isSDCardAvaliable()) {
        // filepath = Environment.getExternalStorageDirectory() + "/";
        // }
        // } else {
        // if (FileUtil.isSDCardAvaliable()) {
        // filepath = context.getExternalCacheDir().getAbsolutePath()
        // + "/";
        // }
        // }
        
        if (isSDCardAvaliable())
        {
            filepath = Environment.getExternalStorageDirectory() + LOG_FILE_PATH;
            if (!new File(filepath).exists())
            {
                new File(filepath).mkdirs();
            }
        }
        
        if (null == filepath)
        {
            return;
        }
        
        File file = new File(filepath, (needWriteFiel + MYLOGFILEName));
        try
        {
            FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * 获取存本地文件名方法
     * 
     * @param appName
     * @param tail
     *            文件名后缀
     * @return
     */
    public static String getLocalFileSavePath(String appName, String tail)
    {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (TextUtils.isEmpty(path))
        {
            path = Environment.getDataDirectory().getAbsolutePath() + "/";
        }
        else
        {
            path += LOG_CRUSH_PATH;
        }
        // long nameLong = System.currentTimeMillis();
        Date nowtime = new Date();
        String name = myLogSdf.format(nowtime);
        path += appName + "/crush";
        File file = new File(path);
        if (!file.exists())
        {
            file.mkdirs();
        }
        path += "/crush-" + name + tail;
        return path;
    }
    
    public static boolean isSDCardAvaliable()
    {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
