package com.dongframe.demo.activity;

import org.json.JSONObject;

import com.dong.frame.view.ViewAttacher;
import com.dongframe.demo.R;
import com.dongframe.demo.dialogs.UpgradeDialog;
import com.dongframe.demo.fragment.HomeFragment;
import com.dongframe.demo.fragment.MessageFragment;
import com.dongframe.demo.fragment.SettingFragment;
import com.dongframe.demo.https.APIServer;
import com.dongframe.demo.https.HttpCallback;
import com.dongframe.demo.https.JsonParsesInfo;
import com.dongframe.demo.infos.Software;
import com.dongframe.demo.utils.LogUtils;
import com.dongframe.demo.utils.SharedUtil;
import com.dongframe.demo.utils.StringUtils;
import com.dongframe.demo.utils.WifigxApUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Response;

public class HomeActivity extends BaseActivity implements OnClickListener
{
    private static final String TAG = HomeActivity.class.getSimpleName();
    
    private static final int FRAGMENT_TAB_HOME = 0;
    
    private static final int FRAGMENT_TAB_MESSAGE = 1;
    
    private static final int FRAGMENT_TAB_SETTING = 2;
    
    private static final String TAB_HOME_NAME = "HOME";
    
    private static final String TAB_MESSAGE_NAME = "MESSAGE";
    
    private static final String TAB_SETTING_NAME = "SETTING";
    
    private FrameLayout tabLayout1, tabLayout2, tabLayout3;
    
    private TextView tabText1, tabText2, tabText3;
    
    private Fragment currentFragment;
    
    private FragmentManager mFManager;
    
    private HomeFragment mHomeFragment;
    
    private MessageFragment mMessageFragment;
    
    private SettingFragment mSettingFragment;
    
    private String currentTabName = "";
    
    private int currentTab = FRAGMENT_TAB_HOME;
    
    private Bundle bundle = new Bundle();
    
    private long lastTime = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ViewAttacher.attach(this);
        initView();
        setListener();
        checkUpgrade();
    }
    
    private void initView()
    {
        tabSelect(currentTab);
    }
    
    private void setListener()
    {
        tabLayout1.setOnClickListener(this);
        tabLayout2.setOnClickListener(this);
        tabLayout3.setOnClickListener(this);
        
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tabLayout1:
                tabSelect(FRAGMENT_TAB_HOME);
                break;
            case R.id.tabLayout2:
                tabSelect(FRAGMENT_TAB_MESSAGE);
                break;
            case R.id.tabLayout3:
                tabSelect(FRAGMENT_TAB_SETTING);
                break;
            default:
                break;
        }
    }
    
    private void tabSelect(int index)
    {
        Fragment fragment = null;
        String desTab = null;
        switch (index)
        {
            case FRAGMENT_TAB_HOME:
                tabText1.setSelected(true);
                tabText2.setSelected(false);
                tabText3.setSelected(false);
                if (null == mHomeFragment)
                {
                    mHomeFragment = HomeFragment.newInstance(FRAGMENT_TAB_HOME);
                }
                fragment = mHomeFragment;
                desTab = TAB_HOME_NAME;
                break;
            case FRAGMENT_TAB_MESSAGE:
                tabText1.setSelected(false);
                tabText2.setSelected(true);
                tabText3.setSelected(false);
                if (null == mMessageFragment)
                {
                    mMessageFragment = MessageFragment.newInstance(FRAGMENT_TAB_MESSAGE);
                }
                fragment = mMessageFragment;
                desTab = TAB_MESSAGE_NAME;
                break;
            case FRAGMENT_TAB_SETTING:
                tabText1.setSelected(false);
                tabText2.setSelected(false);
                tabText3.setSelected(true);
                if (null == mSettingFragment)
                {
                    mSettingFragment = SettingFragment.newInstance(FRAGMENT_TAB_SETTING);
                }
                fragment = mSettingFragment;
                desTab = TAB_SETTING_NAME;
                break;
        }
        
        if (StringUtils.isEmpty(currentTabName) || null == currentFragment)
        {
            initFragment(fragment, desTab);
        }
        else
        {
            showFragment(fragment, desTab);
        }
    }
    
    private void initFragment(Fragment fragment, String desTab)
    {
        LogUtils.LOGV(TAG, "initFragment...");
        currentFragment = fragment;
        currentTabName = desTab;
        mFManager = getSupportFragmentManager();
        FragmentTransaction mFTransaction = mFManager.beginTransaction();
        mFTransaction.add(R.id.fragment_content, currentFragment);
        mFTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        mFTransaction.commit();
    }
    
    private void showFragment(Fragment fragment, String desTab)
    {
        if (!currentTabName.equals(desTab))
        {
            LogUtils.LOGI(TAG, "showFragment（）starts　currentFragment  " + currentFragment);
            LogUtils.LOGI(TAG, "showFragment（）starts　fragment  " + fragment);
            try
            {
                if (currentFragment == fragment)
                {
                    return;
                }
                LogUtils.LOGI(TAG, "mFManager :" + mFManager);
                if (mFManager == null)
                {
                    mFManager = getSupportFragmentManager();
                }
                FragmentTransaction mFTransaction = mFManager.beginTransaction();
                // mFTransaction
                // .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                
                android.support.v4.app.Fragment frag = mFManager.getFragment(bundle, desTab);
                LogUtils.LOGI(TAG, "showFragment（）1");
                if (null != currentFragment)
                {
                    mFManager.saveFragmentInstanceState(currentFragment);
                    mFManager.putFragment(bundle, currentTabName, currentFragment);
                }
                LogUtils.LOGI(TAG, "showFragment（）2");
                if (null != frag)
                {
                    LogUtils.LOGI(TAG, "Use saved Fragment");
                    mFTransaction.show(frag);
                }
                else
                {
                    LogUtils.LOGI(TAG, "Create New Fragment");
                    mFTransaction.add(R.id.fragment_content, fragment);
                    mFTransaction.show(fragment);
                }
                LogUtils.LOGI(TAG, "showFragment（）3");
                if (null != currentFragment)
                {
                    mFTransaction.hide(currentFragment);
                }
                // mFTransaction.addToBackStack(null);
                mFTransaction.commit();
                LogUtils.LOGI(TAG, "showFragment（）4");
                currentFragment = fragment;
                currentTabName = desTab;
                LogUtils.LOGI(TAG, "showFragment（）5");
            }
            catch (Exception e)
            {
                LogUtils.LOGI(TAG, "showFragment（）error " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /** 检测更新
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void checkUpgrade()
    {
        if (!checkNetWork())
        {
            return;
        }
        APIServer.reqUpgrade(this, new HttpCallback()
        {
            
            @Override
            public void onSuccess(int statusCode, String msg, JSONObject jsonObject, Call call, Response response)
            {
                showMessage("检测更新成功");
                Software software = JsonParsesInfo.upgradeParse(jsonObject);
                SharedUtil.setDownLoadUrl(HomeActivity.this, software.getUpdateUrl());
                if (WifigxApUtil.isInitVersion(HomeActivity.this, software))
                {
                    new UpgradeDialog(HomeActivity.this);
                }
            }
            
            @Override
            public void onError(Call call, Exception e)
            {
                showMessage(getString(R.string.net_connect_right) + "(" + e.getMessage() + ")");
                LogUtils.LOGD(TAG,
                    "checkUpgrade==onError==" + getString(R.string.net_connect_right) + "(" + e.getMessage() + ")");
            }
            
            @Override
            public void onFailure(int statusCode, String msg, Call call, Response response)
            {
                showMessage(msg);
                LogUtils.LOGD(TAG, "checkUpgrade==onFailure==" + msg);
            }
            
        });
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            //            LogUtils.LOGE(TAG, "==KEYCODE_BACK==" + lastTime);
            //            long currentTime = System.currentTimeMillis();
            //            if (currentTime - lastTime < 2000)
            //            {
            //                this.finish();
            //            }
            //            else
            //            {
            //                lastTime = currentTime;
            //                showMessage(R.string.again_click_exit);
            //            }
            quitSureDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
    
    private void quitSureDialog()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.dialog_title);
        dialog.setMessage(R.string.dialog_quit_msg);
        dialog.setPositiveButton(R.string.dialog_sure, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                reqLogout();
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.show();
    }
    
    /** 登出
     * <功能详细描述>
     * @param isLogin 验证成功是否登录
     * @see [类、类#方法、类#成员]
     */
    private void reqLogout()
    {
        if (!checkNetWork())
        {
            HomeActivity.this.finish();
            return;
        }
        showLoadDialog();
        APIServer.reqLogout(this, new HttpCallback()
        {
            
            @Override
            public void onSuccess(int statusCode, String msg, JSONObject jsonObject, Call call, Response response)
            {
                hideLoadDialog();
                HomeActivity.this.finish();
            }
            
            @Override
            public void onError(Call call, Exception e)
            {
                showMessage(getString(R.string.net_connect_right) + "(" + e.getMessage() + ")");
                hideLoadDialog();
                HomeActivity.this.finish();
            }
            
            @Override
            public void onFailure(int statusCode, String msg, Call call, Response response)
            {
                showMessage(msg);
                hideLoadDialog();
                HomeActivity.this.finish();
            }
            
        });
    }
    
}
