package com.samehope.plugin.wechat.pushmessage;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.log.Log;
import com.samehope.common.utils.CommonUtil;
import com.samehope.plugin.wechat.model.WechatPushMsgConfig;
import com.samehope.plugin.wechat.utils.oauth2.RefreshAcessToken;

public class WXMsgPushUtil {
	
	private static final Log log = Log.getLog(WXMsgPushUtil.class);
	
	/**微信发送模板消息url**/
	public static final String TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
	/**微信群发消息url**/
	public static final String GROUP_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=";
	
	/**
	 * 推送微信模板消息
	 * @param dataJsonStr	 
	 */
	public static String toPushMsgToUser(WechatPushMsgConfig wechatPushMsgConfig, Map<String, Map<String, String>> map) {
		String appid = wechatPushMsgConfig.getAppid();
		String secret = wechatPushMsgConfig.getSecret();
		String toUser = wechatPushMsgConfig.getTouser();
		String templateId = wechatPushMsgConfig.getTemplateId();
		String url = wechatPushMsgConfig.getUrl();
		String dataJsonStr = createDataJson(toUser, templateId,url, map);
		String access_token = RefreshAcessToken.getAcessToken(appid,secret);
		log.info("access_token=="+access_token);
		String requestURL = TEMPLATE_URL + access_token;
		log.info("dataJsonStr=="+dataJsonStr);
		JSONObject jsonObject = CommonUtil.httpsRequest(requestURL, "POST", dataJsonStr);
		System.out.println(jsonObject.toString());
		return jsonObject.toJSONString();
	}
	
	/**
	 * 构建微信模板消息传输数据格式
	 * @param touser openId
	 * @param template_id 微信模板id
	 * @param url 模板跳转链接
	 * @param map 模板数据
	 * 
	 * dataJsonStr 格式：
	  	"{
	  		\"touser\":\"" + openId + "\",
			\"template_id\":\"66U3XpkNBuey-B99NEWb1wUODUkMWhgYQRCkszjp-ME\", //微信模板id
			\"url\":\""+"xxxxxxxxxx"+"\",	//模板跳转链接
			\"data\":{
				\"first\":{
					\"value\":\"[微团购]：恭喜您已入选试吃达人，四海八荒美食等您品鉴，由于人数较多，我们会为您分期安排，关注公众号消息，使用小助手剁手，福利来的更快。\",
					\"color\":\"#173177\"
				},
				\"keyword1\":{
					\"value\":\"" +"试吃达人第一期"+"\",
					\"color\":\"#173177\"
		 		},
				\"keyword2\":{
					\"value\":\"" +"2017试吃达人评选小组"+"\",
					\"color\":\"#173177\"
		 		}
			}
		 }"
	 */
	public static String createDataJson(String touser, String template_id,String url, Map<String, Map<String, String>> map){
		String dataJsonStr = "";
		dataJsonStr += "{";
		dataJsonStr += "\"touser\":\"" + touser + "\",";
		dataJsonStr += "\"template_id\":\""+template_id+"\","; //微信模板id
		dataJsonStr += "\"url\":\""+url+"\",";//模板跳转链接
		dataJsonStr += "\"data\":{";
		
		if(map != null && map.size() > 0) {
			for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
				dataJsonStr += "\"" + entry.getKey() + "\":{";
				
				Map<String, String> mapData = entry.getValue();
				for(Map.Entry<String, String> entryChild  : mapData.entrySet()){
					dataJsonStr += "\"" + entryChild.getKey() + "\":\"" + entryChild.getValue() + "\",";
				}
				dataJsonStr = dataJsonStr.substring(0, dataJsonStr.length() -1);
				dataJsonStr += "},";
			}
			dataJsonStr = dataJsonStr.substring(0, dataJsonStr.length() -1);
			
		}
					
		dataJsonStr += "}";
		dataJsonStr += " }";
		
		return dataJsonStr;
	}
	
	/**
	 * 微信群发消息
	 * @param msgDataJson
	 */
	public static JSONObject sendGroupWXMsg(String msgDataJson,String appid,String secert){
		String access_token = RefreshAcessToken.getAcessToken(appid,secert);
		System.out.println(access_token);
		String requestURL = GROUP_URL + access_token;
		System.out.println(msgDataJson);
		JSONObject jsonObject = CommonUtil.httpsRequest(requestURL, "POST", msgDataJson);
		System.out.println(jsonObject.toString());
		return jsonObject;
	}
	
	/**
	 * 微信群发消息传输数据模式
	 * @param openIds openid的集合
	 * @param content 消息数据
	 * {
		   "touser":[
		    "OPENID1",
		    "OPENID2"
		   ],
		    "msgtype": "text",
		    "text": { "content": "hello from boxer."}
		}
	 */
	public static String createGroupMsg(List<String> openIds, String content){
		String msgDataJson = "";
		if(openIds != null && openIds.size() > 0) {
			msgDataJson += "{";
			msgDataJson += "\"touser\":[";
			for( int i=0; i<openIds.size(); i++) {
				msgDataJson += "\""+openIds.get(i)+"\",";
			}
			msgDataJson = msgDataJson.substring(0, msgDataJson.length()-1);
			msgDataJson += "],";
			msgDataJson += "\"msgtype\":\"text\",";
			msgDataJson += "\"text\":{\"content\":\"" + content + "\"}";
			msgDataJson += "}";
		}
		return msgDataJson;
	}

}
