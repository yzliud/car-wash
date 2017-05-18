package com.samehope.plugin.wechat.model;

public class WechatPushMsgConfig {
	/**
	 * 公众号appid
	 */
	public String appid;
	/**
	 * 公众号secret
	 */
	public String secret;
	/**
	 * 推送人(openid)
	 */
	public String touser;
	/**
	 * 通知模板id
	 */
	public String templateId;
	/**
	 * 详情跳转url
	 */
	public String url;

	
	public static WechatPushMsgConfig getWechatPushMsgConfig(String appid, String secret, String touser, String templateId, String url) {
		WechatPushMsgConfig wechatPushMsgConfig = new WechatPushMsgConfig();
		wechatPushMsgConfig.setAppid(appid);
		wechatPushMsgConfig.setSecret(secret);
		wechatPushMsgConfig.setTouser(touser);
		wechatPushMsgConfig.setTemplateId(templateId);
		wechatPushMsgConfig.setUrl(url);
		return wechatPushMsgConfig;
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

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
