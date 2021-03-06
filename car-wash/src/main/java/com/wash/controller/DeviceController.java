package com.wash.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.netty.channel.Channel;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.ehcache.CacheKit;
import com.samehope.common.utils.UuidUtils;
import com.samehope.core.render.JsonResult;
import com.samehope.plugin.wechat.WechatKit;
import com.samehope.plugin.wechat.model.WechatPushMsgConfig;
import com.wash.Consts;
import com.wash.Global;
import com.wash.model.WashDevice;
import com.wash.model.WashDeviceCommandBack;
import com.wash.model.WashDeviceRecord;
import com.wash.model.WashMember;
import com.wash.model.WashOrdOrder;

/**
 * 发送洗车开关指令
 * @author Administrator
 *
 */
public class DeviceController extends Controller {

	private static Log log = Log.getLog(DeviceController.class);
	
	/**
	 * 发送指令
	 */
	public void deviceCommand() {
		
		JsonResult jsonResult = new JsonResult();
		String command = getPara("command"); //0-开始接单  1-结束订单  2-单独打开设备 3.单独关闭设备
		String memberId = (String)getSessionAttr("memberIdSession");
		String orderId = getPara("order_id");
		
		WashOrdOrder washOrdOrder = WashOrdOrder.dao.findById(orderId);
		if(null == washOrdOrder){
			jsonResult.setRtnCode(1);
			jsonResult.setRtnMsg("订单不存在");
			renderJson(jsonResult);
		}else{
			WashDevice washDevice = WashDevice.dao.findById(washOrdOrder.getDeviceId());
			if(null == washDevice ){
				jsonResult.setRtnCode(2);
				jsonResult.setRtnMsg("设备不存在");
				renderJson(jsonResult);
			}else{
				if(washDevice.getStatus().equals(Consts.DeviceStatus_1) || washDevice.getStatus().equals(Consts.DeviceStatus_2)){
					jsonResult.setRtnCode(3);
					jsonResult.setRtnMsg("设备("+washDevice.getName()+")故障或下线,请联系系统管理元");
					renderJson(jsonResult);
				}else{
					
					String msg = "";
					Date date = new Date();
					if (command.equals("0")) { 	
						msg = "K"+PropKit.use("system_config.properties").get("duration")+"\n";
						//更新订单洗车工及状
						washOrdOrder.setOrderStatus(Consts.orderStatus_2);
						washOrdOrder.setWashPersonId(memberId);
						washOrdOrder.setUpdateDate(date);
						washOrdOrder.update();
						
						
					}else if (command.equals("1")){
						msg = "G\n";
						//更新订单洗车工及状
						washOrdOrder.setOrderStatus(Consts.orderStatus_3);
						washOrdOrder.setUpdateDate(date);
						washOrdOrder.setEndTime(date);
						washOrdOrder.update();
						
						WashMember wm = WashMember.dao.findById(washOrdOrder.getCarPersonId());
						//发送待评价推送
						String path ="http://" + getRequest().getServerName()+getRequest().getContextPath();
						sendWeMsg(wm.getOpenId(), washOrdOrder, path);
						
					}else if (command.equals("2")){
						msg = "K"+PropKit.use("system_config.properties").get("duration")+"\n";
						
					}else if (command.equals("3")){
						msg = "G\n";
					}
					
					//记录指令信息
					WashDeviceRecord washDeviceRecord = new WashDeviceRecord();
					washDeviceRecord.setId(UuidUtils.getUuid());
					washDeviceRecord.setDeviceId(washDevice.getId());
					washDeviceRecord.setOrderId(orderId);
					washDeviceRecord.setMsg(msg);
					washDeviceRecord.setOperatorTime(date);
					washDeviceRecord.setWashPersionId(memberId);
					washDeviceRecord.setCreateDate(date);
					washDeviceRecord.setUpdateDate(date);
					washDeviceRecord.setDelFlag(Consts.DelFlag_0);
					
					boolean commandFlag = sendCommand(washOrdOrder.getDeviceMac(), msg, washDeviceRecord);
					log.info("指令>>>>> " + command + "||设备>>>>> " + washDevice.getMac()+"---------"+commandFlag);
					
					jsonResult.setRtnCode(0);
					jsonResult.setRtnMsg("操作成功");
					renderJson(jsonResult);
				}
			}
		}
	}
	
	/**
	 * 等待发送指令回参
	 */
	public void checkDeviceCommond(){
		JsonResult jsonResult = new JsonResult();
		String type = getPara("type");
		String memberId = (String)getSessionAttr("memberIdSession");
		String orderId = getPara("order_id");
		WashOrdOrder washOrdOrder = WashOrdOrder.dao.findById(orderId);
		if(null == washOrdOrder){
			jsonResult.setRtnCode(1);
			jsonResult.setRtnMsg("订单不存在");
		}else{
			WashDeviceRecord washDeviceRecord = WashDeviceRecord.dao.findFirst(
					"SELECT * FROM wash_device_record WHERE device_id = ? AND wash_persion_id = ? AND order_id = ? ORDER BY update_date DESC"
					, washOrdOrder.getDeviceId(), memberId, orderId);
			
			if( null == washDeviceRecord){
				jsonResult.setRtnCode(1);
				jsonResult.setRtnMsg("指令不存在");
			}else{
				WashDeviceCommandBack washDeviceCommandBack = WashDeviceCommandBack.dao.findFirst(
						"select * from wash_device_command_back where channel_id = ? and msg = ? and update_date >= ? "
						, washDeviceRecord.getChannelId()
						, "4B"
						, washDeviceRecord.getOperatorTime());
				if(null != washDeviceCommandBack){
					jsonResult.setRtnCode(0);
					jsonResult.setRtnMsg("指令执行成功");
				}else{
					if("1".equals(type)){
						Channel deviceChannel = Global.deviceMap.get(washOrdOrder.getDeviceMac());
						String msg = "K"+PropKit.use("system_config.properties").get("duration")+"\n";
						deviceChannel.writeAndFlush(msg);
					}
					jsonResult.setRtnCode(1);
					jsonResult.setRtnMsg("指令执行失败");
				}
			}
		}
		log.info("checkDeviceCommond>>>>> " +orderId + "" + jsonResult.getRtnMsg());
		renderJson(jsonResult);
		
	}
	
	/**
	 * 发送指令
	 * @param mac
	 * @param msg
	 * @return
	 */
	public static boolean sendCommand(String mac,String msg, WashDeviceRecord washDeviceRecord){
				
		Channel deviceChannel = Global.deviceMap.get(mac);
		boolean flag = false;
		
		if (deviceChannel != null) {
			log.info("DEVICE channel status:" + deviceChannel.isOpen() + "--"
					+ deviceChannel.isActive() + "-"
					+ deviceChannel.isWritable());
			if (deviceChannel.isOpen()) {// 连接是否正常 发送指令
				deviceChannel.writeAndFlush(msg);
				flag = true;
				washDeviceRecord.setChannelId(deviceChannel.id().toString());
				washDeviceRecord.save();
			}
		}
		return flag;
	}

	public void finish() {
		String mac = getPara("device_mac");
		CacheKit.remove("device", mac);
		renderNull();
	}
	
	@Clear
	public static boolean sendWeMsg(String openId, WashOrdOrder washOrdOrder, String path){
        boolean isPush= false;
		if(openId != null && !"".equals(openId)) {
			//推送消息
			Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String,String>>();
			
			Map<String, String> first = new HashMap<String, String>();
			first.put("value", "您好,你的爱车已清洗完毕");
			first.put("color", "#173177");
			dataMap.put("first", first);

			Map<String, String> keyword1 = new HashMap<String, String>();
			keyword1.put("value", washOrdOrder.getOrderNo());
			keyword1.put("color", "#173177");
			dataMap.put("keyword1", keyword1);
			
			
			Map<String, String> keyword2 = new HashMap<String, String>();
			keyword2.put("value", washOrdOrder.getEndTime().toString());
			keyword2.put("color", "#173177");
			dataMap.put("keyword2", keyword2);

			

			Map<String, String> remark = new HashMap<String, String>();
			remark.put("value", "点击详情,您可以对此次洗车进行评价");
			remark.put("color", "#173177");
			dataMap.put("remark", remark);
			
		
			WechatPushMsgConfig wechatPushMsgConfig = WechatPushMsgConfig.getWechatPushMsgConfig(PropKit.use("wx_config.properties").get("appid"), 
					PropKit.use("wx_config.properties").get("secret"), 
					openId, 
					PropKit.use("wx_config.properties").get("template_id"),
					path + PropKit.use("wx_config.properties").get("appraise_jumpurl") + washOrdOrder.getId());
			String pushWXTemplateMsg = WechatKit.pushWXTemplateMsg(wechatPushMsgConfig,dataMap);
			log.info("WXMsg:" + pushWXTemplateMsg);
			isPush= true;
		
		}
		return isPush;
	}

}
