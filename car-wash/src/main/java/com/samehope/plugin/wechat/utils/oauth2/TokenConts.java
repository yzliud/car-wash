package com.samehope.plugin.wechat.utils.oauth2;

public class TokenConts {
	
	public static String AccessToken = "";
	
	public static String AccessExpires = "";

	public static String getAccessToken() {
		return AccessToken;
	}

	public static void setAccessToken(String accessToken) {
		AccessToken = accessToken;
	}

	public static String getAccessExpires() {
		return AccessExpires;
	}

	public static void setAccessExpires(String accessExpires) {
		AccessExpires = accessExpires;
	}
	
}
