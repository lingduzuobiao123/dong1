package com.dongframe.demo.activity;

import com.dongframe.demo.R;
import com.dongframe.demo.utils.WifigxApUtil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity
{
    private Dialog proDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    
    public void initLoadDialog()
    {
        proDialog = new ProgressDialog(this);
        proDialog.setTitle(R.string.dialog_progress);
    }
    
    public void showLoadDialog()
    {
        if (null == proDialog)
        {
            initLoadDialog();
        }
        
        if (null != proDialog && !proDialog.isShowing())
        {
            proDialog.show();
        }
    }
    
    public void hideLoadDialog()
    {
        if (null != proDialog && proDialog.isShowing())
        {
            proDialog.dismiss();
        }
    }
    
    public void showMessage(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    
    public void showMessage(int msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 检查网络情况
     * 
     * @return true：可用，false：不可用，且跳出对话框提示
     */
    public boolean checkNetWork()
    {
        //无网络不可用
        if (!WifigxApUtil.isNetConnected(this))
        {
            showMessage(R.string.net_not_available);
            return false;
        }
        else
        {
            return true;
        }
    }
}
