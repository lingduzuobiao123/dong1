package com.dongframe.demo.activity;

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
        proDialog.setTitle("加载中...");
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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    
    public void showMessage(int msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
