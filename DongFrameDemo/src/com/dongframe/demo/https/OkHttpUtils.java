package com.dongframe.demo.https;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.dongframe.demo.R;
import com.dongframe.demo.utils.LogUtils;
import com.dongframe.demo.utils.StringUtils;
import com.dongframe.demo.utils.WifigxApUtil;

import android.app.Activity;
import android.content.Context;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils
{
    private static final String TAG = OkHttpUtils.class.getSimpleName();
    
    private volatile static OkHttpUtils mInstance;
    
    private OkHttpClient mOkHttpClient;
    
    /**
     * ua
     */
    private String userAgent;
    
    public OkHttpUtils()
    {
        mOkHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .cookieJar(new SimpleCookieJar())
            //            .cookieJar(new CookieJar()
            //            {
            //                final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
            //                
            //                @Override
            //                public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
            //                {
            //                    cookieStore.put(url, cookies);
            //                }
            //                
            //                @Override
            //                public List<Cookie> loadForRequest(HttpUrl url)
            //                {
            //                    List<Cookie> cookies = cookieStore.get(url);
            //                    return cookies != null ? cookies : new ArrayList<Cookie>();
            //                }
            //            })
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
    
    /**
    * 取消请求
    * @param tag
    */
    public void cancelTag(Object tag)
    {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
    }
    
    /** 异步get请求
     * <功能详细描述>
     * @param url
     * @param callback
     * @see [类、类#方法、类#成员]
     */
    public void asyncGet(Context context, String url, HttpCallback callback)
    {
        asyncGet(context, url, TAG, callback);
    }
    
    /** 异步get请求
     * <功能详细描述>
     * @param url
     * @param callback
     * @see [类、类#方法、类#成员]
     */
    public void asyncGet(Context context, String url, Object tag, HttpCallback callback)
    {
        Request request =
            new Request.Builder().get().header("User-Agent", userAgent(context)).url(url).tag(tag).build();
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
        asyncPost(context, url, requestBody, TAG, callback);
    }
    
    /** 异步Post请求
     * <功能详细描述>
     * @param url
     * @param callback
     * @see [类、类#方法、类#成员]
     */
    public void asyncPost(Context context, String url, RequestBody requestBody, Object tag, HttpCallback callback)
    {
        Request requestPost =
            new Request.Builder().header("User-Agent", userAgent(context)).url(url).post(requestBody).tag(tag).build();
        mOkHttpClient.newCall(requestPost).enqueue(new ReqCallback(context, callback));
    }
    
    /** 设置User-Agent
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String userAgent(Context context)
    {
        if (StringUtils.isEmpty(userAgent))
        {
            userAgent = "Dong-Frame/" + WifigxApUtil.getAppVersionName(context) + "(Android;"
                + WifigxApUtil.getManufacturer() + ";" + WifigxApUtil.getType() + ";" + WifigxApUtil.getOSVer() + ";"
                + WifigxApUtil.getIMEIId(context) + ")";
        }
        return userAgent;
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
            InputStream inputStream = null;
            try
            {
                if (response.isSuccessful())
                {
                    inputStream = response.body().byteStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer sb = new StringBuffer();
                    String resultStr = "";
                    String line = "";
                    while (null != (line = reader.readLine()))
                    {
                        sb.append(line + "\n");
                        //                        resultStr += line;
                    }
                    resultStr = sb.toString();
                    //                    resultStr = response.body().toString();
                    LogUtils.LOGI(TAG, "response==" + resultStr);
                    final JSONObject jsonObject = new JSONObject(resultStr);
                    final JSONObject resultObject = jsonObject.optJSONObject("result");
                    final int statusCode = resultObject.optInt("status", -2);
                    final String msg = resultObject.optString("msg", context.getString(R.string.http_failure));
                    if (statusCode == 0)
                    {
                        ((Activity)context).runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (null != callback)
                                {
                                    callback.onSuccess(statusCode, msg, jsonObject, call, response);
                                }
                            }
                        });
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
            finally
            {
                if (null != inputStream)
                {
                    inputStream.close();
                }
            }
        }
        
    }
}
