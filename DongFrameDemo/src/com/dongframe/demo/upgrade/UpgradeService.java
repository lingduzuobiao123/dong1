package com.dongframe.demo.upgrade;

import java.io.File;

import com.dongframe.demo.R;
import com.dongframe.demo.infos.Software;
import com.dongframe.demo.interfaces.OnDownloadListener;
import com.dongframe.demo.utils.FileUtil;
import com.dongframe.demo.utils.LogUtils;
import com.dongframe.demo.utils.SharedUtil;
import com.dongframe.demo.utils.WifigxApUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

public class UpgradeService extends Service
{
    private static final String TAG = UpgradeService.class.getSimpleName();
    
    /**
     * 下载结束发送Handler
     */
    public static final int DOWNLOAD_COMPLETE = 0x0050;
    
    /**
     * 开始下载初始化通知栏
     */
    public static final int SHOW_DOWN_APK_PROGRESS = 0x0051;
    
    /**
     * apk下载的action
     */
    public static final String SERVICE_DOWNLOAD_APK = "com.wifigx.wifishare.download_apk";
    
    /**
     * apk下载中的action
     */
    public static final String SERVICE_DOWNLOAD_APK_ING = "com.wifigx.wifishare.download_apking";
    
    /**
     * 下载进度
     */
    public static final String PROGRESS_PERCENT = "DOWNLOAD_APK_PROGESS";
    
    /**
     * 下载结果 1下载完成
     */
    private static final String DOWNLOAD_RESULT = "download_result";
    
    /**
     * 是否后台下载，后台下载是在wifi网络下 下载 通知栏不提升，下载完成不提升安装 true后台下载
     */
    public static final String DOWNLOAD_HIDE_KEY = "download_hide_apk";
    
    /**
     * 是否后台下载，后台下载是在wifi网络下 下载 通知栏不提升，下载完成不提升安装
     * true后台下载  
     */
    private boolean downApkHide = false;
    
    /**
     * 下载地址
     */
    private String downloadUrl = "";
    
    private String downApkTitle = "";
    
    private NotificationManager downApkNotifiManager;
    
    private Notification downApkNotification;
    
    /**
     * 通知id
     */
    private static final int DOWNAPKNOTIFIID = 15;
    
    /**
     * 下载进度
     */
    private int mUpgradeProgressValue = -1;
    
    /**
     * 是否下载中
     */
    private boolean isDownLoading = false;
    
    /**
     * 是否下载中
     *
     * @return true下载中
     */
    public boolean isDownLoading()
    {
        return (isDownLoading && !downApkHide);
    }
    
    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    private Handler handler = new Handler()
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case SHOW_DOWN_APK_PROGRESS:
                    initDownApkProgress();
                    updateDownApkProgress(0);
                    break;
                case DOWNLOAD_COMPLETE:
                    clearDownApkNotifi();
                    Bundle data = msg.getData();
                    boolean flag = data.getBoolean(DOWNLOAD_HIDE_KEY, false);
                    String result = data.getString(DOWNLOAD_RESULT);
                    downDoneApk(result, flag);
                    freeMessage(msg.what);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    
    private void freeMessage(int what)
    {
        handler.removeMessages(what);
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (null == intent)
        {
            LogUtils.LOGE(TAG, "====onStartCommand===null ==intent");
            return super.onStartCommand(intent, flags, startId);
        }
        String action = intent.getAction();
        if (action.equals(SERVICE_DOWNLOAD_APK))
        {
            Software software = SharedUtil.getSoftUpdate(UpgradeService.this);
            if (null != software)
            {
                String url = software.getUpdateUrl();
                // downApkTitle = software.getTitle();
                downApkTitle = this.getString(R.string.version_noti_title);
                downApkHide = intent.getBooleanExtra(DOWNLOAD_HIDE_KEY, false);
                downLoadApk(url, software.getApkSize());
            }
        }
        return super.onStartCommand(intent, Service.START_REDELIVER_INTENT, startId);
    }
    
    /**
     * 下载apk
     *
     * @param url
     * @param size
     *            是否后台下载 true后台下载
     */
    private void downLoadApk(String url, final long size)
    {
        LogUtils.LOGI(TAG, "=====downLoadApk====");
        if (isDownLoading)
        {
            return;
        }
        try
        {
            isDownLoading = true;
            downloadUrl = url;
            new Thread()
            {
                @Override
                public void run()
                {
                    boolean downloadSuccess = false;//是否下载完成
                    if (FileUtil.isExistApkFile(UpgradeService.this)
                        && SharedUtil.isUpgradeFileDownloaded(UpgradeService.this))
                    {
                        downloadSuccess = true;
                        Message m = handler.obtainMessage();
                        Bundle data = new Bundle();
                        data.putString(DOWNLOAD_RESULT, downloadSuccess ? "1" : "0");
                        data.putBoolean(DOWNLOAD_HIDE_KEY, downApkHide);
                        m.setData(data);
                        m.what = DOWNLOAD_COMPLETE;
                        handler.sendMessage(m);
                        isDownLoading = false;
                    }
                    else
                    {
                        if (downApkHide)
                        {
                            if (!WifigxApUtil.isWIFIConnected(UpgradeService.this))
                            {
                                return;
                            }
                        }
                        else
                        {
                            handler.sendEmptyMessage(SHOW_DOWN_APK_PROGRESS);
                        }
                        try
                        {
                            downloadSuccess = HttpClientUtil.downloadApkFile(downloadUrl,
                                Context.MODE_WORLD_READABLE,
                                getApplicationContext(),
                                new OnDownloadListener()
                                {
                                    @Override
                                    public boolean onDownload(long totalSize, long downloadSize)
                                    {
                                        if (!downApkHide)
                                        {
                                            return UpgradeService.this.onDownload(totalSize, downloadSize);
                                        }
                                        else
                                        {
                                            return true;
                                        }
                                    }
                                },
                                size);
                            LogUtils.LOGI(TAG, "downloadSuccess = " + downloadSuccess);
                        }
                        catch (Exception e)
                        {
                            downloadSuccess = false;
                            LogUtils.LOGE(TAG, "download apk Exception " + e.getMessage());
                        }
                        finally
                        {
                            Message m = handler.obtainMessage();
                            Bundle data = new Bundle();
                            data.putString(DOWNLOAD_RESULT, downloadSuccess ? "1" : "0");
                            data.putBoolean(DOWNLOAD_HIDE_KEY, downApkHide);
                            m.setData(data);
                            m.what = DOWNLOAD_COMPLETE;
                            handler.sendMessage(m);
                            isDownLoading = false;
                        }
                    }
                }
            }.start();
        }
        catch (Exception e)
        {
            LogUtils.LOGE(TAG, e.getMessage() + "");
            e.printStackTrace();
            isDownLoading = false;
        }
    }
    
    // 更改通知栏里的下载进度
    public boolean onDownload(long totalSize, long downloadSize)
    {
        LogUtils.LOGI(TAG, "==totalSize===" + totalSize + "===downloadSize===" + downloadSize);
        if (!downApkHide && downApkNotifiManager == null && downApkNotification == null)
        {
            initDownApkProgress();
        }
        if (downloadSize >= 0 && totalSize > 0 && downApkNotifiManager != null && downApkNotification != null)
        {
            int tmp = (int)(downloadSize * 100 / totalSize);
            if (mUpgradeProgressValue != tmp)
            {
                mUpgradeProgressValue = tmp;
                updateDownApkProgress(mUpgradeProgressValue);
                // Addded by Li XuanLin on 2012-12-03
                Intent intentS = new Intent();
                intentS.setAction(SERVICE_DOWNLOAD_APK_ING);
                intentS.putExtra(PROGRESS_PERCENT, mUpgradeProgressValue);
                UpgradeService.this.sendBroadcast(intentS);
            }
            // LogUtils.LOGI(TAG, "正在下载进度：" + mUpgradeProgressValue);
            return true;
        }
        return false;
    }
    
    /**
     * apk下载完成
     *
     * @param result 下载结果 1下载成功 @see DOWNLOAD_RESULT
     * @param flag 是否为后台下载  @see downApkHide
     */
    private void downDoneApk(String result, boolean flag)
    {
        /* 下载成功，自动安装 */
        if (result.equals("1"))
        {
            File f = FileUtil.getApkFile(UpgradeService.this);
            if (f != null && f.length() > 0 && f.exists() && FileUtil.isCompleteApk(UpgradeService.this, f.getPath()))
            {
                SharedUtil.setUpgradeFileDownloaded(UpgradeService.this, true);
                if (!flag)
                {
                    // 存储已下载APK版本
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(FileUtil.getApkFile(UpgradeService.this)),
                        "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
            else
            {
                FileUtil.deleteApkFile(UpgradeService.this);
                SharedUtil.setUpgradeFileDownloaded(UpgradeService.this, false);
                SharedUtil.setSoftUpdateApkSize(UpgradeService.this, 0);
                Toast.makeText(UpgradeService.this, R.string.upgrade_fail_txt, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
        }
    }
    
    private void initDownApkProgress()
    {
        downApkNotifiManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        downApkNotification = new Notification(android.R.drawable.stat_sys_download,
            downApkTitle + getResources().getString(R.string.serivce_start_download), System.currentTimeMillis());
        
        // FIXME 这边跳往HomeActivity，之前的是SurfNewsActivity
        // 设置下载过程中，点击通知栏，回到主界面
        // Intent updateIntent = new Intent(this, MainActivity.class);
        // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
        // updateIntent, 0);
        // downApkNotification.contentIntent = pendingIntent;
        downApkNotification.flags = Notification.FLAG_NO_CLEAR;
        if (android.os.Build.VERSION.SDK_INT > 8)
        {
            downApkNotification.contentView =
                new RemoteViews(getApplication().getPackageName(), R.layout.notifi_download_layout);
        }
        else
        {
            downApkNotification.contentView =
                new RemoteViews(getApplication().getPackageName(), R.layout.notifi_download_layout_low_version);
        }
        final Intent emptyIntent = new Intent();
        PendingIntent mPendingIntent =
            PendingIntent.getActivity(this, 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Intent updateIntent = new Intent(this, MainActivity.class);
        // PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0,
        // updateIntent, 0);
        downApkNotification.contentIntent = mPendingIntent;
        downApkNotification.flags = Notification.FLAG_NO_CLEAR;
    }
    
    private void updateDownApkProgress(int progress)
    {
        // 设置通知栏显示内容
        if (android.os.Build.VERSION.SDK_INT > 8)
        {
            downApkNotification.contentView.setProgressBar(R.id.notifi_download_pb, 100, progress, false);
            downApkNotification.contentView.setTextViewText(R.id.notifi_download_tv, downApkTitle);
            downApkNotification.contentView.setTextViewText(R.id.notifi_download_percent, progress + "%");
        }
        else
        {
            downApkNotification.contentView.setProgressBar(R.id.notifi_download_pb_low_version, 100, progress, false);
            downApkNotification.contentView.setTextViewText(R.id.notifi_download_tv_low_version, downApkTitle);
            downApkNotification.contentView.setTextViewText(R.id.notifi_download_percent_low_version, progress + "%");
        }
        // 发出通知
        downApkNotifiManager.notify(DOWNAPKNOTIFIID, downApkNotification);
    }
    
    private void clearDownApkNotifi()
    {
        if (null != downApkNotifiManager)
        {
            downApkNotifiManager.cancel(DOWNAPKNOTIFIID);
            downApkNotifiManager = null;
            downApkNotification = null;
        }
    }
    
    @Override
    public void onDestroy()
    {
        clearDownApkNotifi();
        super.onDestroy();
    }
    
}
