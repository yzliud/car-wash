package com.samehope.plugin.wechat.config;

public class ApiConfig {
	// 根据openId查询用户信息
	public static final String getUserByOpenId = "select * from p_user pu WHERE pu.open_id = ?";
}
