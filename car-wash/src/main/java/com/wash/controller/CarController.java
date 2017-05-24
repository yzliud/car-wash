package com.wash.controller;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.samehope.core.render.JsonResult;
import com.samehope.plugin.ali.Sms;
import com.wash.model.WashMember;

public class CarController extends Controller{
	
	private static Log log = Log.getLog(CarController.class);
	
	/**
	 * 发送验证码
	 */
	public void sendCode(){
		String mobile = getPara("mobile");
		int code = (int) (Math.random() * 9000 + 1000);
//		String content = "您的验证码是：";
//		String msg = content + code;
//		Sms.send(msg, mobile);
		List<String> mobileList = new ArrayList<String>();
		mobileList.add(mobile);
		Sms.sendMsg(mobileList, code+"");
		setSessionAttr("codeSession", code+"");
		log.info("code="+code);
		renderNull();
	}
	
	/**
	 * 绑定手机
	 */
	public void bind(){
		WashMember washMember = getSessionAttr("memberDataSession");
		String mobile = getPara("mobile");
		String code = getPara("code");
		String codeSession = getSessionAttr("codeSession");
		JsonResult jsonResult = new JsonResult();
		if(codeSession.equals(code)){
			washMember.setMobile(mobile);
			boolean ispass = washMember.update();
			if(ispass){
				jsonResult.setRtnCode(0);
				jsonResult.setRtnMsg("绑定成功");
			}else{
				jsonResult.setRtnCode(1);
				jsonResult.setRtnMsg("绑定失败,系统无法连接");
			}
		}else{
			jsonResult.setRtnCode(2);
			jsonResult.setRtnMsg("验证码不一致，请重新输入");
		}
		log.info("bind "+jsonResult.toString());
		renderJson(jsonResult);
	}
	
	/**
	 * 跳转个人信息
	 */
	public void forwardUserInfo(){
		WashMember washMember = getSessionAttr("memberDataSession");
		setAttr("washMember", washMember);
		render("userInfo.html");
	}
	
	/**
	 * 跳转个人中心
	 */
	public void forwardUserCenter(){
		WashMember washMember = getSessionAttr("memberDataSession");
		setAttr("washMember", washMember);
		render("userCenter.html");
	}
	
	/**
	 * 绑定手机
	 */
	public void editUserInfo(){
		WashMember washMember = getSessionAttr("memberDataSession");
		
		String carNumber = getPara("car_number");
		String carModel = getPara("car_model");

		washMember.setCarNumber(carNumber);
		washMember.setCarModel(carModel);
		
		JsonResult jsonResult = new JsonResult();
		boolean ispass = washMember.update();
		if(ispass){
			jsonResult.setRtnCode(0);
			jsonResult.setRtnMsg("操作成功");
			setSessionAttr("memberDataSession", washMember);
		}else{
			jsonResult.setRtnCode(1);
			jsonResult.setRtnMsg("操作失败,系统无法连接");
		}
		
		renderJson(jsonResult);
	}

}
