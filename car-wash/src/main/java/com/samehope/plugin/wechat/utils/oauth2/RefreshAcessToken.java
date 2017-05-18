package com.samehope.plugin.wechat.utils.oauth2;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.samehope.common.utils.CommonUtil;
import com.samehope.common.utils.DateUtils;
import com.samehope.common.utils.StringUtils;

public class RefreshAcessToken{
	
	public static Map<String,Map<String,String> > accessTokenMap = new HashMap<String,Map<String,String> >();
	
	public static String getAcessToken(String appid, String secret) {
		Map<String, String> map = accessTokenMap.get(appid);
		String accessToken = "";
		String expireTime = "";
		if (map != null) {
			accessToken = map.get("accessToken");
			expireTime = map.get("expireTime");
		}else{
			map = new HashMap<String,String>();
		}
		if (StringUtils.isBlank(accessToken) || DateUtils.getDistanceTimes(expireTime, DateUtils.now()) >= 2) {
			String requestUrl = WeChatUtils.getGlobalAccessTokenURL(appid, secret);
			JSONObject accessObjstr = CommonUtil.httpsRequest(requestUrl, "GET", null);
			String newAccessToken = "";
			if (null != accessObjstr && !StringUtils.isBlank(accessObjstr.getString("access_token"))) {
				newAccessToken = accessObjstr.getString("access_token");
				map.put("accessToken", newAccessToken);
				map.put("expireTime", DateUtils.now());
				accessTokenMap.put(appid, map);
				accessToken = newAccessToken;
			}
		}
		return accessToken;
	}
}