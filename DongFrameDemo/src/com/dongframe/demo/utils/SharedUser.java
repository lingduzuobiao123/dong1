package com.dongframe.demo.utils;

import com.dongframe.demo.infos.Software;
import com.dongframe.demo.infos.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedUser
{
    private static final String TAG = SharedUser.class.getSimpleName();
    
    public final static String SP_NAME_USER = "SP_NAME_USER";
    
    // 版本更新
    private static final String SP_USER_ID = "user_id";
    
    private static final String SP_USER_USERNAME = "user_username";
    
    private static final String SP_USER_PASSWORD = "user_password";
    
    private static final String SP_USER_USERTYPE = "user_usertype";
    
    private static final String SP_USER_ADDTIME = "user_addtime";
    
    private static final String SP_USER_AREACODE = "user_areacode";
    
    private static final String SP_USER_AREANAME = "user_areaname";
    
    private static final String SP_USER_MOBILE = "user_mobile";
    
    private static final String SP_USER_COMPANY = "user_company";
    
    private static final String SP_USER_REMARK = "user_remark";
    
    private static final String SP_USER_STATE = "user_state";
    
    private static final String SP_USER_ALIASNAME = "user_aliasname";
    
    private static final String SP_USER_PARENTUSERID = "user_parentUserId";
    
    private static final String SP_USER_CONDITIONUSERNAME = "user_conditionUserName";
    
    /** 保存用户信息
     * <功能详细描述>
     * @param context
     * @param user
     * @see [类、类#方法、类#成员]
     */
    public static void setUserMsg(Context context, User user)
    {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_USER, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(SP_USER_ID, user.getId());
        editor.putString(SP_USER_USERNAME, user.getUsername());
        editor.putString(SP_USER_PASSWORD, user.getPassword());
        editor.putInt(SP_USER_USERTYPE, user.getUsertype());
        editor.putLong(SP_USER_ADDTIME, user.getAddtime());
        editor.putString(SP_USER_AREACODE, user.getAreacode());
        editor.putString(SP_USER_AREANAME, user.getAreaname());
        editor.putString(SP_USER_MOBILE, user.getMobile());
        editor.putString(SP_USER_COMPANY, user.getCompany());
        editor.putString(SP_USER_REMARK, user.getRemark());
        editor.putInt(SP_USER_STATE, user.getState());
        editor.putString(SP_USER_ALIASNAME, user.getAliasname());
        editor.putInt(SP_USER_PARENTUSERID, user.getParentUserId());
        editor.putString(SP_USER_CONDITIONUSERNAME, user.getConditionUserName());
        editor.commit();
        
    }
    
    /**
     * 获取用户信息
     * 
     * @param context
     */
    public static User getUserMsg(Context context)
    {
        User user = new User();
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_USER, 0);
        user.setId(sp.getInt(SP_USER_ID, 0));
        user.setUsername(sp.getString(SP_USER_USERNAME, ""));
        user.setPassword(sp.getString(SP_USER_PASSWORD, ""));
        user.setUsertype(sp.getInt(SP_USER_USERTYPE, 0));
        user.setAddtime(sp.getLong(SP_USER_ADDTIME, 0));
        user.setAreacode(sp.getString(SP_USER_AREACODE, ""));
        user.setAreaname(sp.getString(SP_USER_AREANAME, ""));
        user.setMobile(sp.getString(SP_USER_MOBILE, ""));
        user.setCompany(sp.getString(SP_USER_COMPANY, ""));
        user.setRemark(sp.getString(SP_USER_REMARK, ""));
        user.setState(sp.getInt(SP_USER_STATE, 0));
        user.setAliasname(sp.getString(SP_USER_ALIASNAME, ""));
        user.setParentUserId(sp.getInt(SP_USER_PARENTUSERID, 0));
        user.setConditionUserName(sp.getString(SP_USER_CONDITIONUSERNAME, ""));
        return user;
    }
    
}
