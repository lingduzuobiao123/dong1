package com.dongframe.demo.https;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dongframe.demo.DongApplication;
import com.dongframe.demo.infos.Software;
import com.dongframe.demo.infos.User;

public class JsonParsesInfo {

	/**
	 * 登录成功后解析
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static User loginParse(JSONObject jsonObject) {
		JSONObject jsonb = jsonObject.optJSONObject("userData");
		JSONObject jsonUser = jsonb.optJSONObject("user");
		User user = new User();
		user.setId(jsonUser.optInt("id"));
		user.setUsername(jsonUser.optString("username"));
		user.setPassword(jsonUser.optString("password"));
		user.setUsertype(jsonUser.optInt("usertype"));
		user.setAddtime(jsonUser.optLong("addtime"));
		user.setAreacode(jsonUser.optString("areacode"));
		user.setAreaname(jsonUser.optString("areaname"));
		user.setMobile(jsonUser.optString("mobile"));
		user.setCompany(jsonUser.optString("company"));
		user.setRemark(jsonUser.optString("remark"));
		user.setState(jsonUser.optInt("state"));
		user.setAliasname(jsonUser.optString("aliasname"));
		user.setParentUserId(jsonUser.optInt("parentUserId"));
		user.setConditionUserName(jsonUser.optString("conditionUserName"));
		JSONArray jsonRight = jsonb.optJSONArray("userRight");
		if (null != jsonRight && jsonRight.length() > 0) {
			Map<String, String> rightMap = new HashMap<String, String>();
			for (int i = 0; i < jsonRight.length(); i++) {
				String key = jsonRight.optString(i);
				rightMap.put(key, key);
			}
			DongApplication.getInstance().setRightMap(rightMap);
		}
		return user;
	}
	
	/**
	 * 获取APP版本信息成功后解析
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static Software upgradeParse(JSONObject jsonObject) {
		JSONObject jsonb = jsonObject.optJSONObject("userData");
		Software software = new Software();
		
		return software;
	}
}
