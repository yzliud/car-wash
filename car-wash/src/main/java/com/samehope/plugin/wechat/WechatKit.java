package com.samehope.plugin.wechat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.samehope.common.utils.MD5Util;
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
        StringBuffer url = request.getRequestURL(); 
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).toString(); 
        String backUri = tempContextUrl+"/wechat/oauth2/toOauth2Page?jsonStr="+jsonObj.toJSONString();
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
		StringBuffer url = request.getRequestURL(); 
	    String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).toString();
	        
		String toJspayUrl = tempContextUrl + "/wechat/pay/toJspayOuth2";
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
	  * 是否微信V3签名,规则是:按参数名称a-z排序,遇到空值的参数不参加签名
	  * 传入微信返回信息解析后的SortedMap格式参数数据
	  * 验证消息是否是微信发出的合法消息
	  * @param smap
	  * @param apiKey 设置的密钥
	  * @return 验证结果
	  */
	 @SuppressWarnings("rawtypes")
	 public static boolean isWechatSign(SortedMap<String, String> smap,String apiKey) {
	     StringBuffer sb = new StringBuffer();
	     Set es = smap.entrySet();
	     Iterator it = es.iterator();
	     while (it.hasNext()) {
	         Map.Entry entry = (Map.Entry) it.next();
	         String k = (String) entry.getKey();
	         String v = (String) entry.getValue();
	         if (!"sign".equals(k) && null != v && !"".equals(v) && !"key".equals(k)) {
	             sb.append(k + "=" + v + "&");
	         }
	     }
	     sb.append("key=" + apiKey);
	     /** 验证的签名 */
	     String sign = MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
	     /** 微信端返回的合法签名 */
	     String validSign = ((String) smap.get("sign")).toUpperCase();
	     log.info("sign================="+sign);
	     log.info("validSign================="+validSign);
	     return validSign.equals(sign);
	 }
	
	/**
	 * 支付成功后，执行回调方法，对支付结果验签
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	public static SortedMap<String,String> checkPayResult(HttpServletRequest request) throws IOException{
		 InputStream inStream = request.getInputStream();
		 ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		 byte[] buffer = new byte[1024];
		 int len = 0;
		 while ((len = inStream.read(buffer)) != -1) {
		   outSteam.write(buffer, 0, len);
		 }
		 outSteam.close();
		 inStream.close();
		 /** 获取微信调用notify_url的返回XML信息 */
		 String result = new String(outSteam.toByteArray(), "utf-8");
		
		 SortedMap<String, String> dom4jXMLParse = null;
		 try {
			dom4jXMLParse = dom4jXMLParse(result);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		 Set es = dom4jXMLParse.entrySet();
	     Iterator it = es.iterator();
	     while (it.hasNext()) {
	         Map.Entry entry = (Map.Entry) it.next();
	         String k = (String) entry.getKey();
	         String v = (String) entry.getValue();
	         log.info("key========"+k + "  "+"value========"+v);
	     }
		 return dom4jXMLParse;
	}
	
	/**
	  * 传入微信回调返回的XML信息
	  * 以Map形式返回便于取值
	  * dom4j解析XML,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值为空
	  * @param strXML
	  * @return
	  * @throws DocumentException 
	  */
	 @SuppressWarnings("rawtypes")
	 public static SortedMap<String, String> dom4jXMLParse(String    strXML) throws DocumentException {
	     SortedMap<String, String> smap = new TreeMap<String, String>();
	     org.dom4j.Document doc = DocumentHelper.parseText(strXML);
	     org.dom4j.Element root = doc.getRootElement();
	     for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
	    	 org.dom4j.Element e = (org.dom4j.Element) iterator.next();
	         smap.put(e.getName(), e.getText());
	     }
	     return smap;
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
