package com.samehope.plugin.wechat;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.samehope.plugin.wechat.model.WechatConfig;
import com.samehope.plugin.wechat.model.WechatPushMsgConfig;
import com.samehope.plugin.wechat.pushmessage.WXMsgPushUtil;
import com.samehope.plugin.wechat.utils.oauth2.WeChatUtils;

/**
 * 微信服务接口
 * @author taoliangfei
 *
 */
public class WechatKit extends Controller{

	private static final Log log = Log.getLog(WechatKit.class);

	/**
	 * 微信授权接口
	 * @param wechatConfig
	 * @param controllerKey
	 * @param methodName
	 */
	public static String toUserOuth2(WechatConfig wechatConfig,HttpServletRequest request,HttpServletResponse response, String controllerKey, String methodName){
		JSONObject jsonObj = new JSONObject();
        jsonObj.put("appid", wechatConfig.getAppid());
        jsonObj.put("secret", wechatConfig.getSecret());
        jsonObj.put("methodName", methodName);
        jsonObj.put("controllerKey", controllerKey);
        jsonObj.put("device_mac", request.getParameter("device_mac"));
        log.info(jsonObj.toJSONString());
        String getWeiXinCodeUrl = WeChatUtils.getCallBackCodeURL(wechatConfig.getAppid());
        String backUri = "http://"+request.getServerName()+request.getContextPath()+"/wechat/oauth2/toOauth2Page?jsonStr="+jsonObj.toJSONString();
        getWeiXinCodeUrl = getWeiXinCodeUrl.replace("REDIRECT_URI", backUri);
        log.info("getWeiXinCodeUrl=="+getWeiXinCodeUrl);
        try {
			response.sendRedirect(getWeiXinCodeUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	
	/**
	 * 微信支付接口
	 * @param wechatConfig
	 * @param controllerKey
	 * @param methodName
	 * orderId payMoney methodName
	 */
	public static void toJspayOuth2(WechatConfig wechatConfig,Map<String,String> map,HttpServletRequest request,HttpServletResponse response){
		String servletPath = request.getServletPath();
		String[] splitPath = servletPath.split("\\/");
		String controllerKey = "/"+splitPath[1]+"/"+splitPath[2];
		String methodName = map.get("methodName");//splitPath[3];
		String toJspayUrl = "http://"+request.getServerName() + request.getContextPath() + "/wechat/pay/toJspayOuth2";
        log.info("toJspayUrl=="+toJspayUrl+"?controllerKey="+controllerKey+"&methodName="+methodName+"&orderId="+map.get("orderId")+"&payMoney="+map.get("payMoney")
        +"&appId="+wechatConfig.getAppid()+"&secret="+wechatConfig.getSecret()+"&partner="+wechatConfig.getPartner()+"&partnerkey="+wechatConfig.getPartnerkey()
        +"&serverName="+request.getServerName()+"&openid="+wechatConfig.getOpenid());
        try {
			response.sendRedirect(toJspayUrl+"?controllerKey="+controllerKey+"&methodName="+methodName+"&orderId="+map.get("orderId")+"&payMoney="+map.get("payMoney")
			+"&appId="+wechatConfig.getAppid()+"&secret="+wechatConfig.getSecret()+"&partner="+wechatConfig.getPartner()+"&partnerkey="+wechatConfig.getPartnerkey()
			+"&serverName="+request.getServerName()+"&openId="+wechatConfig.getOpenid());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 推送消息
	 * @param wechatPushMsgConfig 配置信息
	 * @param map 推送内容
	 * @return
	 */
	public static String pushWXTemplateMsg(WechatPushMsgConfig wechatPushMsgConfig,Map<String, Map<String, String>> map){
		return WXMsgPushUtil.toPushMsgToUser(wechatPushMsgConfig, map);
	}
}
