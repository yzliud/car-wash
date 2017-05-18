package com.samehope.plugin.wechat.model;

/**
 * 微信公众号信息
 * 
 * @author taoliangefei
 *
 */
public class WechatConfig {
	/**
	 * 用户openid
	 */
	public String openid;
	/**
	 * 微信公众号appid
	 */
	public String appid = "";
	/**
	 * 微信公众号secret
	 */
	public String secret = "";
	/**
	 * 微信公众号js支付partner
	 */
	public String partner = "";
	/**
	 * 微信公众号js支付partnerkey
	 */
	public String partnerkey = "";

	/**
	 * 获取公众号信息
	 * 
	 * @param appid
	 * @param secret
	 * @param partner
	 * @param partnerkey
	 * @param openid
	 * @return
	 */
	public static WechatConfig getWechatConfig(String appid, String secret, String partner, String partnerkey,
			String openid) {
		WechatConfig wechatConfig = new WechatConfig();
		wechatConfig.setAppid(appid);
		wechatConfig.setSecret(secret);
		wechatConfig.setPartner(partner);
		wechatConfig.setPartnerkey(partnerkey);
		wechatConfig.setOpenid(openid);
		return wechatConfig;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getPartnerkey() {
		return partnerkey;
	}

	public void setPartnerkey(String partnerkey) {
		this.partnerkey = partnerkey;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

}
