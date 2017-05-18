package com.samehope.plugin.wechat.utils.jspay;

public class MD5SignUtil {
	public static String sign(String content, String key)
            throws Exception{
        String signStr = "";
        signStr = content + "&key=" + key;
        return MD5Util.MD5(signStr).toUpperCase();
    }
}
