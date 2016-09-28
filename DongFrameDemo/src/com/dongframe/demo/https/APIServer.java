package com.dongframe.demo.https;

import android.content.Context;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class APIServer
{
    /**
     * 
     */
    public static String cookie;
    
    /**
     * 请求地址头
     */
    public final static String XIANMWANG = "http://61.130.8.18:8889/backoffice/";
    
    /**
     * 二次验证 <功能详细描述>
     * 
     * @param userName
     * @param userPwd
     * @see [类、类#方法、类#成员]
     */
    public static void reqTwoVerify(Context context, String pwd2, HttpCallback callback)
    {
        String url = XIANMWANG + "doPwd2.json?pwd2=" + pwd2;
        OkHttpUtils.getInstance().asyncGet(context, url, callback);
    }
    
    /**
     * 升级 <功能详细描述>
     * 
     * @param userName
     * @param userPwd
     * @see [类、类#方法、类#成员]
     */
    public static void reqUpgrade(Context context, HttpCallback callback)
    {
        String url = XIANMWANG + "user_getVersion.action";
        OkHttpUtils.getInstance().asyncGet(context, url, callback);
    }
    
    /**
     * 登录接口 <功能详细描述>
     * 
     * @param userName
     * @param userPwd
     * @see [类、类#方法、类#成员]
     */
    public static void reqLogin(Context context, String userName, String userPwd, HttpCallback callback)
    {
        String url = XIANMWANG + "login.json?userName=" + userName + "&userPwd=" + userPwd;
        OkHttpUtils.getInstance().asyncGet(context, url, callback);
    }
    
    /**
     * 登出接口 <功能详细描述>
     * 
     * @param userName
     * @param userPwd
     * @see [类、类#方法、类#成员]
     */
    public static void reqLogout(Context context, HttpCallback callback)
    {
        String url = XIANMWANG + "logout.json";
        OkHttpUtils.getInstance().asyncGet(context, url, callback);
    }
    
    /**
     * 获取上级区域树 <功能详细描述>
     * 
     * @param userName
     * @param userPwd
     * @see [类、类#方法、类#成员]
     */
    public static void reqUpArea(Context context, HttpCallback callback)
    {
        String url = XIANMWANG + "getUpArea.json";
        OkHttpUtils.getInstance().asyncGet(context, url, callback);
    }
    
    /**
     * 登录接口 <功能详细描述>
     * 
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
