package com.dongframe.demo.fragment;

import com.dongframe.demo.R;
import com.dongframe.demo.utils.WifigxApUtil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment
{
    private Dialog proDialog;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    
    @Override
    public void onDestroyView()
    {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }
    
    public void initLoadDialog()
    {
        proDialog = new ProgressDialog(getActivity());
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
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    
    public void showMessage(int msg)
    {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 检查网络情况
     * 
     * @return true：可用，false：不可用，且跳出对话框提示
     */
    public boolean checkNetWork()
    {
        //无网络不可用
        if (!WifigxApUtil.isNetConnected(getActivity()))
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
