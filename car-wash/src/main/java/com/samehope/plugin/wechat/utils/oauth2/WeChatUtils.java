package com.samehope.plugin.wechat.utils.oauth2;

import java.net.URLEncoder;

import com.jfinal.kit.PropKit;

public class WeChatUtils {
	
	public static String project_name = "";
	public static String scope = "snsapi_userinfo";
	public static String response_type = "code";
	public static String state = "123";
	public static String grant_type= "authorization_code";
	public final static String WEIXIN_CODE_OPENID_URL ="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=RESPONSE_TYPE&scope=SCOPE&state=STATE#wechat_redirect"; 
	public final static String WEIXIN_ACCESS_TOKEN_OPENID_URL ="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	public final static String WEIXIN_ACCESS_USERINFO_URL ="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN"; 
	public final static String WEIXIN_GLOBAL_ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
	
	static{
		project_name = PropKit.use("wx_config.properties").get("project_name").trim();
	}
	
	/**
	 * 用户同意授权，获取code的URL
	 * @return
	 */
    public static String getCallBackCodeURL(String appid){
        String resultURL = WEIXIN_CODE_OPENID_URL.replace("APPID", urlEnodeUTF8(appid));
        resultURL = resultURL.replace("RESPONSE_TYPE", response_type);
        resultURL = resultURL.replace("SCOPE", scope);
        resultURL = resultURL.replace("STATE", state);
        return resultURL;
    }
    
    /**
	 * 获取网页授权accessTokenURL
	 * @return
	 */
    public static String getAccessTokenURL(String appid,String secret){
        String resultURL = WEIXIN_ACCESS_TOKEN_OPENID_URL.replace("APPID", urlEnodeUTF8(appid));
        resultURL = resultURL.replace("SCOPE", scope);
        resultURL = resultURL.replace("SECRET", secret);
        return resultURL;
    }

    /**
	 * 获取用户信息
	 * @return
	 */
    public static String getUserInfoByTokenURL(String accessToken,String openId){
        String resultURL = WEIXIN_ACCESS_USERINFO_URL.replace("ACCESS_TOKEN", accessToken);
        resultURL = resultURL.replace("OPENID", openId);
        return resultURL;
    }
    
    
    /**
	 * 获取全局AccessToken
	 * @return
	 */
    public static String getGlobalAccessTokenURL(String appid,String secret){
        String resultURL = WEIXIN_GLOBAL_ACCESSTOKEN_URL.replace("APPID", urlEnodeUTF8(appid));
        resultURL = resultURL.replace("SECRET", secret);
        return resultURL;
    }
    
    //进行转码
    public static String urlEnodeUTF8(String str){
        String result = str;
        try {
            result = URLEncoder.encode(str,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
