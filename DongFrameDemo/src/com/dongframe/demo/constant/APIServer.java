package com.dongframe.demo.constant;

import com.dongframe.demo.https.HttpCallback;
import com.dongframe.demo.https.OkHttpUtils;

import android.content.Context;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class APIServer
{
    public final static String XIANMWANG = "http://61.130.8.18:8889/backoffice/";
    
    /** 二次验证
     * <功能详细描述>
     * @param userName
     * @param userPwd
     * @see [类、类#方法、类#成员]
     */
    public static void reqTwoVerify(Context context, String pwd2, HttpCallback callback)
    {
        String url = XIANMWANG + "doPwd2.json?pwd2=" + pwd2;
        OkHttpUtils.getInstance().asyncGet(context, url, callback);
    }
    
    /** 二次验证
     * <功能详细描述>
     * @param userName
     * @param userPwd
     * @see [类、类#方法、类#成员]
     */
    public static void reqTwoVerify1(Context context, String pwd2, HttpCallback callback)
    {
        String url = XIANMWANG + "doPwd2.json?";
        RequestBody requestBody = new FormBody.Builder().add("pwd2", pwd2).build();
        OkHttpUtils.getInstance().asyncPost(context, url, requestBody, callback);
    }
    
    /** 登录接口
     * <功能详细描述>
     * @param userName
     * @param userPwd
     * @see [类、类#方法、类#成员]
     */
    public static void reqLogin(Context context, String userName, String userPwd, HttpCallback callback)
    {
        String url = XIANMWANG + "login.json?userName=" + userName + "&userPwd=" + userPwd;
        OkHttpUtils.getInstance().asyncGet(context, url, callback);
    }
    
    /** 登录接口
     * <功能详细描述>
     * @param userName
     * @param userPwd
     * @see [类、类#方法、类#成员]
     */
    public static void reqLogin1(Context context, String userName, String userPwd, HttpCallback callback)
    {
        String url = XIANMWANG + "login.json?";
        RequestBody requestBody = new FormBody.Builder().add("userName", userName).add("userPwd", userPwd).build();
        OkHttpUtils.getInstance().asyncPost(context, url, requestBody, callback);
    }
}
