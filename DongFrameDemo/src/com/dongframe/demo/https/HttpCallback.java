package com.dongframe.demo.https;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public interface HttpCallback
{
    void onSuccess(int statusCode, JSONObject jsonObject, Call call, Response response);
    
    void onError(Call call, Exception e);
    
    void onFailure(int statusCode, String msg, Call call, Response response);
}
