package com.samehope.common.utils;

import com.jfinal.plugin.redis.Redis;

public class RedisUtils {
	
	public static void set1(String cacheName, String key, Object value, int expireSeconds, String plugnName){
		Redis.use(plugnName).set(cacheName + "-" + key, value);
		Redis.use(plugnName).expire(cacheName + "-" + key, expireSeconds);
	}
	
	public static void del1(String cacheName, String key, String plugnName){
		Redis.use(plugnName).del(cacheName + "-" + key);
	}
	
	public static <T> T get1(String cacheName, String key, String plugnName){
		return Redis.use(plugnName).get(cacheName + "-" + key);
	}

}
