package com.dongframe.demo.activity;

import java.io.IOException;

import com.dong.frame.view.ViewAttacher;
import com.dongframe.demo.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements OnClickListener
{
    private TextView textView1, textView2;
    
    private Button button1, button2, button3, button4;
    
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    textView1.setText((String)msg.obj);
                    break;
                case 2:
                    textView2.setText((String)msg.obj);
                    break;
                case 3:
                    hideLoadDialog();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewAttacher.attach(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button1:
                getAsynHttp();
                break;
            case R.id.button2:
                getAsynHttp1();
                break;
            case R.id.button3:
                postAsynHttp();
                break;
            case R.id.button4:
                
                break;
            
            default:
                break;
        }
    }
    
    private void getAsynHttp()
    {
        showMessage("使用方法HTTP GET 方法二");
        showLoadDialog();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url("http://www.baidu.com");
        //可以省略，默认是GET请求
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        Call mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                MainActivity.this.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mHandler.sendEmptyMessage(3);
                    }
                });
            }
            
            @Override
            public void onResponse(Call call, Response response)
                throws IOException
            {
                //不是在UI线程中运行的。
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);
                Headers responseHeaders = response.headers();
                final StringBuffer sb = new StringBuffer();
                for (int i = 0, size = responseHeaders.size(); i < size; i++)
                {
                    sb.append(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    sb.append("\n");
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                
                if (null != response.cacheResponse())
                {
                    String str = response.cacheResponse().toString();
                    Log.i("wangshu", "cache---" + str);
                }
                else
                {
                    response.body().string();
                    String str = response.networkResponse().toString();
                    Log.i("wangshu", "network---" + str);
                }
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mHandler.dispatchMessage(mHandler.obtainMessage(1, sb.toString()));
                        mHandler.sendEmptyMessage(3);
                        Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    
    /** Https的get请求
     * 双方验证
     * 不需要加线程
     * @see [类、类#方法、类#成员]
     */
    private void getAsynHttp1()
    {
        showMessage("使用方法HTTP GET 方法一");
        showLoadDialog();
        Request request = new Request.Builder().url("http://publicobject.com/helloworld.txt").build();
        new OkHttpClient().newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                MainActivity.this.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mHandler.sendEmptyMessage(3);
                    }
                });
            }
            
            @Override
            public void onResponse(Call call, final Response response)
                throws IOException
            {////不是在UI线程中运行的。
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);
                Headers responseHeaders = response.headers();
                final StringBuffer sb = new StringBuffer();
                for (int i = 0, size = responseHeaders.size(); i < size; i++)
                {
                    sb.append(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    sb.append("\n");
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                final String bodyStr = response.body().string();
                MainActivity.this.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mHandler.dispatchMessage(mHandler.obtainMessage(1, sb.toString()));
                        mHandler.dispatchMessage(mHandler.obtainMessage(2, bodyStr));
                        System.out.println(bodyStr);
                        mHandler.sendEmptyMessage(3);
                    }
                });
            }
        });
    }
    
    private void postAsynHttp()
    {
        showMessage("异步POST请求");
        showLoadDialog();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("size", "10").build();
        Request request =
            new Request.Builder().url("http://api.1-blog.com/biz/bizserver/article/list.do").post(formBody).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                MainActivity.this.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mHandler.sendEmptyMessage(3);
                    }
                });
            }
            
            @Override
            public void onResponse(Call call, Response response)
                throws IOException
            {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);
                Headers responseHeaders = response.headers();
                final StringBuffer sb = new StringBuffer();
                for (int i = 0, size = responseHeaders.size(); i < size; i++)
                {
                    sb.append(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    sb.append("\n");
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                final String bodyStr = response.body().string();
                Log.i("wangshu", bodyStr);
                
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mHandler.dispatchMessage(mHandler.obtainMessage(1, sb.toString()));
                        mHandler.dispatchMessage(mHandler.obtainMessage(2, bodyStr));
                        mHandler.sendEmptyMessage(3);
                    }
                });
            }
            
        });
    }
}
