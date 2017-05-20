package com.wash.interceptor;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.samehope.common.utils.Emoji;
import com.samehope.common.utils.StringUtils;
import com.samehope.common.utils.UuidUtils;
import com.samehope.plugin.wechat.WechatKit;
import com.samehope.plugin.wechat.model.SNSUserInfo;
import com.samehope.plugin.wechat.model.WechatConfig;
import com.wash.model.WashMember;

public class WebChatOauthInterceptorByIntro implements Interceptor {
	
	private static final Log log = Log.getLog(WebChatOauthInterceptorByIntro.class);
	
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		log.debug(inv.getControllerKey()+ "---"+ inv.getMethodName());
		HttpServletRequest request = controller.getRequest();
		String device_mac = request.getParameter("device_mac");
		log.info("device_mac="+device_mac);
		
		/***********************/
		SNSUserInfo ui = new SNSUserInfo();
		ui.setOpenId("ofUWXwwH318V2HCwZJLPQJ0dKFZI");
		ui.setNickname("liu");
		ui.setHeadImgUrl("http://wx.qlogo.cn/mmopen/rQx3vkJTWe6rYqZjzWibWuMvkpX7KXhYMb92MtShHMzeCCict7hGGQ49A0VL4q97UibicYro4Bko54AIdsxkRfAgZ62LgyzBAPuj/0");
		controller.setSessionAttr("openIdSession", "ofUWXwwH318V2HCwZJLPQJ0dKFZI");
		controller.setSessionAttr("openMemberSession", ui);
		/***********************/
		
		String openId = (String)controller.getSessionAttr("openIdSession");
		SNSUserInfo userInfo = (SNSUserInfo)controller.getSessionAttr("openMemberSession");
		log.info("openId="+openId);		
		
		if(StringUtils.isBlank(openId)){
			log.info("/wechat/oauth2/toUserOuth2?controllerKey="+inv.getControllerKey()+"&methodName="+inv.getMethodName());
			WechatConfig wechatConfig = WechatConfig.getWechatConfig(PropKit.use("wx_config.properties").get("appid"), PropKit.use("wx_config.properties").get("secret"), "", "","");
			WechatKit.toUserOuth2(wechatConfig,request,controller.getResponse(),inv.getControllerKey(),inv.getMethodName());
		}else{
			if(userInfo != null && userInfo.getOpenId() != null){
				saveOrUpdate(userInfo,controller);
			}
			//判断是否是黑名单用户
			WashMember wm = WashMember.dao.findFirst("select * from wash_member WHERE open_id = ? ", openId);
			if(wm != null && wm.getStatus().equals("1")){
				try {
					StringBuffer url = request.getRequestURL();  
					String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).toString(); 
					controller.getResponse().sendRedirect(tempContextUrl+"/error/black");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			inv.invoke();
		}
	}
	
	
	/**
	 * 保存或更新用户信息
	 * @param userInfo
	 */
	public void saveOrUpdate(SNSUserInfo userInfo,Controller controller){
			String openId = userInfo.getOpenId();
			String nickname = Emoji.filterEmoji(userInfo.getNickname());
			String sex = userInfo.getSex()+"";
			String country = userInfo.getCountry();
			String province = userInfo.getProvince();
			String city = userInfo.getCity();
			String headImgUrl = userInfo.getHeadImgUrl();
			
			WashMember washMember = WashMember.dao.findFirst("select * from wash_member WHERE open_id = ? ", openId);
			if (null == washMember ) {
				washMember = new WashMember();
				washMember.setId(UuidUtils.getUuid());
				washMember.setOpenId(openId);
		    	washMember.setNickName(nickname);
		    	washMember.setSex(sex);
		    	washMember.setCountry(country);
		    	washMember.setProvince(province);
		    	washMember.setCity(city);
		    	washMember.setImg(headImgUrl);
		    	washMember.setUserType("0");
		    	washMember.setCreateDate(new Date());
		    	washMember.setUpdateDate(new Date());
		    	washMember.setDelFlag("0");
		    	washMember.save();
			}else{
		    	washMember.setOpenId(openId);
		    	washMember.setNickName(nickname);
		    	washMember.setSex(sex);
		    	washMember.setCountry(country);
		    	washMember.setProvince(province);
		    	washMember.setCity(city);
		    	washMember.setImg(headImgUrl);
		    	washMember.setLastTime(new Date());
		    	washMember.update();
		    }
			controller.setSessionAttr("memberIdSession", washMember.getId());
			
			controller.setSessionAttr("memberDataSession", washMember);
	}
	
}
