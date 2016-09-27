package com.dongframe.demo.fragment;

import org.json.JSONObject;

import com.dongframe.demo.R;
import com.dongframe.demo.constant.APIServer;
import com.dongframe.demo.https.HttpCallback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import okhttp3.Call;
import okhttp3.Response;

public class HomeFragment extends BaseFragment
{
    
    /**
     * 静态工厂方法需要一个int型的值来初始化fragment的参数，
     * 然后返回新的fragment到调用者 
     */
    public static HomeFragment newInstance(int tab)
    {
        HomeFragment f = new HomeFragment();
        //        Bundle args = new Bundle();
        //        args.putInt("tab", tab);
        //        f.setArguments(args);
        return f;
    }
    
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
        getDataFromNet();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    private void getDataFromNet()
    {
        if (!checkNetWork())
        {
            return;
        }
        showLoadDialog();
        APIServer.reqUpArea(getActivity(), new HttpCallback()
        {
            
            @Override
            public void onSuccess(int statusCode, String msg, JSONObject jsonObject, Call call, Response response)
            {
                
                hideLoadDialog();
            }
            
            @Override
            public void onError(Call call, Exception e)
            {
                showMessage(getString(R.string.net_connect_right) + "(" + e.getMessage() + ")");
                hideLoadDialog();
            }
            
            @Override
            public void onFailure(int statusCode, String msg, Call call, Response response)
            {
                showMessage(msg);
                hideLoadDialog();
            }
            
        });
        
    }
    
}
