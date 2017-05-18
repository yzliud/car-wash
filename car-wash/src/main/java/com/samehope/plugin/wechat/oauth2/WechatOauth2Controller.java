package com.samehope.plugin.wechat.oauth2;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.samehope.common.utils.CommonUtil;
import com.samehope.plugin.wechat.model.SNSUserInfo;
import com.samehope.plugin.wechat.model.WeixinOauth2Token;
import com.samehope.plugin.wechat.utils.oauth2.WeChatUtils;

/**
 * 微信授权相关接口
 * @author taoliangfei
 *
 */
//@RouterMapping(url="/wechat/oauth2",viewPath="")
@Clear
public class WechatOauth2Controller extends Controller{
	
	private static final Log log = Log.getLog(WechatOauth2Controller.class);
	
	/**
	 * 跳转至访问的页面
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void toOauth2Page() throws IOException{
		String jsonStr = getPara("jsonStr");
		log.info("before=====jsonStr========"+jsonStr);
		int indexPos = jsonStr.indexOf("{\"");
		if (indexPos < 0) {
			jsonStr = jsonStr.replace("{", "{\"");
			jsonStr = jsonStr.replace(":", "\":\"");
			jsonStr = jsonStr.replace(",", "\",\"");
			jsonStr = jsonStr.replace("}", "\"}");
		}
		log.info("after======jsonStr========"+jsonStr);
		JSONObject jsonObj = JSON.parseObject(jsonStr);
		String controllerKey = jsonObj.getString("controllerKey");
		String methodName = jsonObj.getString("methodName");
		log.info("jumpAction=" + controllerKey + "/" + methodName);
		String appid = jsonObj.getString("appid");
		log.info("appid="+jsonObj.getString("appid"));
		String secret = jsonObj.getString("secret");
		log.info("secret"+jsonObj.getString("secret"));
		String code = getPara("code");
		log.info("code="+code);
		WeixinOauth2Token weixinOauth2Token = getOauth2AccessToken(code,appid,secret);
        // 网页授权接口访问凭证
        String accessToken = weixinOauth2Token.getAccessToken();
        log.info("accessToken="+accessToken);
        // 用户标识
        String openId = weixinOauth2Token.getOpenId();
        log.info("openId="+openId);
        // 获取用户信息
        SNSUserInfo snsUserInfo = getSNSUserInfo(accessToken, openId);
        setSessionAttr("openIdSession", openId);
        setSessionAttr("openMemberSession", snsUserInfo);
        //String userInfoJson = JSON.toJSONString(snsUserInfo);  
        String device_mac = jsonObj.getString("device_mac");
		redirect(controllerKey + "/" + methodName + "?device_mac=" + device_mac);
	}
	
	/**
     * 获取网页授权凭证       
     * 
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code
     * @return WeixinAouth2Token
     */
    public static WeixinOauth2Token getOauth2AccessToken(String code,String appid,String secret) {
        WeixinOauth2Token wat = null;
        String requestUrl = WeChatUtils.getAccessTokenURL(appid,secret);
        requestUrl = requestUrl.replace("APPID", appid);
        requestUrl = requestUrl.replace("SECRET", secret);
        requestUrl = requestUrl.replace("CODE", code);
        // 获取网页授权凭证
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
                wat = new WeixinOauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(Integer.valueOf((String)jsonObject.getString("expires_in")));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                wat = null;
                log.error("获取网页授权凭证失败 errmsg:{}", e);
            }
        }
        return wat;
    }
	
    /**
     * 通过网页授权获取用户信息
     * 
     * @param accessToken 网页授权接口调用凭证
     * @param openId 用户标识
     * @return SNSUserInfo
     */
    public static SNSUserInfo getSNSUserInfo(String accessToken, String openId) {
        SNSUserInfo snsUserInfo = null;
        // 拼接请求地址
        String requestUrl = WeChatUtils.getUserInfoByTokenURL(accessToken,openId);
        // 通过网页授权获取用户信息
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
                snsUserInfo = new SNSUserInfo();
                // 用户的标识
                snsUserInfo.setOpenId(jsonObject.getString("openid"));
                // 昵称
                snsUserInfo.setNickname(jsonObject.getString("nickname"));
                // 性别（1是男性，2是女性，0是未知）
                snsUserInfo.setSex(Integer.valueOf((String)jsonObject.getString("sex")));
                // 用户所在国家
                snsUserInfo.setCountry(jsonObject.getString("country"));
                // 用户所在省份
                snsUserInfo.setProvince(jsonObject.getString("province"));
                // 用户所在城市
                snsUserInfo.setCity(jsonObject.getString("city"));
                // 用户头像
                snsUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
                // 用户特权信息
                //snsUserInfo.setPrivilegeList(JSONArray.toList(jsonObject.getJSONArray("privilege"), List.class));
            } catch (Exception e) {
                snsUserInfo = null;
                log.error("获取用户信息失败 ", e);
            }
        }
        return snsUserInfo;
    }
}
