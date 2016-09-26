package com.dongframe.demo.https;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.dongframe.demo.R;
import com.dongframe.demo.utils.LogUtils;

import android.app.Activity;
import android.content.Context;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils
{
    private static final String TAG = OkHttpUtils.class.getSimpleName();
    
    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
    
    private volatile static OkHttpUtils mInstance;
    
    private OkHttpClient mOkHttpClient;
    
    public OkHttpUtils()
    {
        mOkHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .cookieJar(new CookieJar()
            {
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
                {
                    cookieStore.put(url, cookies);
                }
                
                @Override
                public List<Cookie> loadForRequest(HttpUrl url)
                {
                    List<Cookie> cookies = cookieStore.get(url);
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            })
            .build();
    }
    
    public static OkHttpUtils initClient()
    {
        if (mInstance == null)
        {
            synchronized (OkHttpUtils.class)
            {
                if (mInstance == null)
                {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }
    
    public static OkHttpUtils getInstance()
    {
        return initClient();
    }
    
    public OkHttpClient getOkHttpClient()
    {
        return mOkHttpClient;
    }
    
    /** 异步get请求
     * <功能详细描述>
     * @param url
     * @param callback
     * @see [类、类#方法、类#成员]
     */
    public void asyncGet(Context context, String url, HttpCallback callback)
    {
        Request request = new Request.Builder().get().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new ReqCallback(context, callback));
    }
    
    /** 异步Post请求
     * <功能详细描述>
     * @param url
     * @param callback
     * @see [类、类#方法、类#成员]
     */
    public void asyncPost(Context context, String url, RequestBody requestBody, HttpCallback callback)
    {
        Request requestPost = new Request.Builder().url(url).post(requestBody).build();
        mOkHttpClient.newCall(requestPost).enqueue(new ReqCallback(context, callback));
    }
    
    class ReqCallback implements Callback
    {
        private HttpCallback callback;
        
        private Context context;
        
        public ReqCallback(Context context, HttpCallback callback)
        {
            this.callback = callback;
            this.context = context;
        }
        
        @Override
        public void onFailure(final Call call, final IOException e)
        {
            ((Activity)context).runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (null != callback)
                    {
                        callback.onError(call, e);
                    }
                }
            });
        }
        
        @Override
        public void onResponse(final Call call, final Response response)
            throws IOException
        {
            try
            {
                if (response.isSuccessful())
                {
                    InputStream inputStream = response.body().byteStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String result = "";
                    String line = "";
                    while (null != (line = reader.readLine()))
                    {
                        result += line;
                        
                    }
                    System.out.println(result);
                    
                    String jsonStr = response.body().toString();
                    LogUtils.LOGE(TAG, "response==" + jsonStr);
                    final JSONObject jsonObject = new JSONObject(jsonStr).optJSONObject("result");
                    final int statusCode = jsonObject.optInt("status", -2);
                    if (statusCode == 0)
                    {
                        ((Activity)context).runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (null != callback)
                                {
                                    callback.onSuccess(statusCode, jsonObject, call, response);
                                }
                            }
                        });
                    }
                    else
                    {
                        final String msg = jsonObject.optString("msg", context.getString(R.string.http_failure));
                        ((Activity)context).runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (null != callback)
                                {
                                    callback.onFailure(statusCode, msg + "(" + statusCode + ")", call, response);
                                }
                            }
                        });
                    }
                }
                else
                {
                    ((Activity)context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (null != callback)
                            {
                                callback.onFailure(-1,
                                    context.getString(R.string.net_connect_right) + "(-1)",
                                    call,
                                    response);
                            }
                        }
                    });
                }
            }
            catch (final Exception e)
            {
                ((Activity)context).runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (null != callback)
                        {
                            callback.onError(call, e);
                        }
                    }
                });
            }
        }
        
    }
}
