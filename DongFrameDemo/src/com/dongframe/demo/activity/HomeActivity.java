package com.dongframe.demo.activity;

import com.dong.frame.view.ViewAttacher;
import com.dongframe.demo.R;
import com.dongframe.demo.fragment.HomeFragment;
import com.dongframe.demo.fragment.MessageFragment;
import com.dongframe.demo.fragment.SettingFragment;
import com.dongframe.demo.utils.LogUtils;
import com.dongframe.demo.utils.StringUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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
                    mHomeFragment = new HomeFragment(FRAGMENT_TAB_HOME);
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
                    mMessageFragment = new MessageFragment(FRAGMENT_TAB_MESSAGE);
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
                    mSettingFragment = new SettingFragment(FRAGMENT_TAB_SETTING);
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
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            LogUtils.LOGE(TAG, "==KEYCODE_BACK==" + lastTime);
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime < 2000)
            {
                this.finish();
            }
            else
            {
                lastTime = currentTime;
                showMessage(R.string.again_click_exit);
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
    
}
