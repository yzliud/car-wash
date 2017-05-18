package com.samehope.common.utils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import com.jfinal.kit.HashKit;

public class EncryptUtils extends HashKit {

	public static String salt() {
		int random = (int) (10 + (Math.random() * 10));
		return UUID.randomUUID().toString().replace("-", "").substring(random);// 随机长度
	}

	public static String encryptPassword(String password, String salt) {
		return sha256(password + salt);
	}

	public static boolean verlifyUser(String userPassword,String userSalt, String password) {

		if (userPassword == null)
			return false;

		if (userSalt == null) {
			return false;
		}
		return userPassword.equals(encryptPassword(password, userSalt));
	}

	public static String generateUcode(BigInteger id,String salt) {
		return md5(id + salt);
	}

	public static String signForRequest(Map<String, String> params,String secret) {
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);

		StringBuilder query = new StringBuilder();
		query.append(secret);
		for (String key : keys) {
			String value = params.get(key);
			if (StringUtils.areNotEmpty(key, value)) {
				query.append(key).append(value);
			}
		}
		query.append(secret);
		return HashKit.md5(query.toString()).toUpperCase();
	}

	public static void main(String[] args) {
		System.out.println(encryptPassword("123456", "abc"));
	}

}
