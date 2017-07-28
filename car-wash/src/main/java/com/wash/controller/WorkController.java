package com.wash.controller;

import org.jsoup.helper.StringUtil;

import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.samehope.core.render.JsonResult;
import com.wash.Consts;
import com.wash.model.WashDevice;
import com.wash.model.WashMember;
import com.wash.model.WashWorkPerson;
import com.wash.service.WorkService;

/**
 * 扫码上下班
 * @author Administrator
 *
 */
public class WorkController extends Controller {

	private static Log log = Log.getLog(DeviceController.class);
	
	/**
	 * 上班
	 * wash_work_person 设置当前设备的洗车工
	 * wash_work_device_record 记录洗车工的上班记录
	 */
	public void forwardWork(){
		log.info("跳转页面");
		JsonResult jsonResult = new JsonResult();
		String mac = getPara("device_mac");
		String memberId = (String)getSessionAttr("memberIdSession");
		WashMember washMember = (WashMember)getSessionAttr("memberDataSession");
		
		//判断是否是洗车工
		if(washMember!=null && (washMember.getUserType().equals(Consts.UserType_1)||washMember.getUserType().equals(Consts.UserType_2))){
			//MAC 为空时 查询工作表
			if(StringUtil.isBlank(mac)){
				WashWorkPerson washWorkPerson = WashWorkPerson.dao.findFirst("SELECT * FROM wash_work_person WHERE wash_person_id = ? ", memberId);
				if(washWorkPerson != null && washWorkPerson.getWashDeviceMac() != null){
					mac = washWorkPerson.getWashDeviceMac();
				}
			}
			WashDevice washDevice = WashDevice.dao.findFirst("select * from wash_device WHERE mac = ? ", mac);
			if(null != washDevice){
				//设备是否在线
				if(null != washDevice.getStatus() && Consts.DeviceStatus_0.equals(washDevice.getStatus())){
					WashWorkPerson washWorkPerson =  WashWorkPerson.dao.findById(washDevice.getId());
					
					if(washWorkPerson != null){
						//判断当前设备的洗车工是否是本人
						if(washWorkPerson.getWashPersonId().equals(memberId)){
							jsonResult.setRtnCode(10);
							jsonResult.setRtnMsg("下班");
						}else{
							jsonResult.setRtnCode(11);
							jsonResult.setRtnMsg("上班");
						}
					}else{
						jsonResult.setRtnCode(11);
						jsonResult.setRtnMsg("上班");
					}
				}else{
					jsonResult.setRtnCode(1);
					jsonResult.setRtnMsg("设备故障或离线，请联系管理员");
				}
			}else{
				jsonResult.setRtnCode(2);
				jsonResult.setRtnMsg("设备不存在，请联系管理员");
			}
			if(washMember.getUserType().equals(Consts.UserType_2)){
				jsonResult.setRtnCode(20);
			}
			setAttr("jsonResult", jsonResult);
			
			//最近的上班记录
//			List<Record> workRecordList = Db.find("select a.*, b.name device_name from wash_work_device_record a,wash_device b where a.wash_person_id = ? and a.wash_device_id = b.id  order by a.update_date desc LIMIT 0, 3", memberId);
//			setAttr("workRecordList", workRecordList);
			
			//昨天订单数
			long yesterdayCount = Db.queryLong("select count(1) from wash_ord_order where wash_person_id = ? and date_format(end_time,'%Y-%m-%d')=date_format(DATE_SUB(curdate(), INTERVAL 1 day),'%Y-%m-%d') and order_status in(3,9) ", memberId );
			setAttr("yesterdayCount", yesterdayCount);
			
			//当天
			long nowCount = Db.queryLong("select count(1) from wash_ord_order where wash_person_id = ? and DATE_FORMAT(end_time,'%Y-%m-%d')=DATE_FORMAT(NOW(),'%Y-%m-%d') and order_status in(3,9) ", memberId );
			setAttr("nowCount", nowCount);
			
			//近7天
			long lastWeekCount = Db.queryLong("select count(1) from wash_ord_order where wash_person_id = ? and date_format(DATE_SUB(NOW(), INTERVAL 7 DAY),'%Y-%m-%d') <= DATE_FORMAT(end_time,'%Y-%m-%d') and order_status in(3,9) ", memberId );
			setAttr("lastWeekCount", lastWeekCount);
			
			//排队
			//long waitCount = Db.queryLong("select count(1) from wash_ord_order where device_mac = ? and DATE_FORMAT(pay_time,'%Y-%m-%d')=DATE_FORMAT(NOW(),'%Y-%m-%d') and order_status in(1,2) ", mac );
			long waitCount = Db.queryLong("select count(1) from wash_ord_order a where  (SELECT 1 FROM wash_work_person p WHERE p.wash_person_id = ? and a.device_id = p.id) and order_status in(1,2) and del_flag = 0 ", memberId );
			setAttr("waitCount", waitCount);
			
			//好评数
			long highPraiseCount = Db.queryLong("SELECT COUNT(1) FROM wash_ord_evaluate a ,wash_ord_order b WHERE a.id = b.id AND a.flag in(0, 1) ");
			setAttr("highPraiseCount", highPraiseCount);
			
			//差评数
			long badPraiseCount = Db.queryLong("SELECT COUNT(1) FROM wash_ord_evaluate a ,wash_ord_order b WHERE a.id = b.id AND a.flag = 3 ");
			setAttr("badPraiseCount", badPraiseCount);
			
			//排队订单
//			List<Record> waitList = Db.find("select * from wash_ord_order where device_mac = ? and DATE_FORMAT(pay_time,'%Y-%m-%d')=DATE_FORMAT(NOW(),'%Y-%m-%d') and order_status in(1,2) order by update_date desc limit 0,3 ", mac);
//			setAttr("waitList", waitList);
			
			//个人信息
			setAttr("washMember", (WashMember)getSessionAttr("memberDataSession"));
			
			setAttr("device_mac", mac);
			
			render("work.html");
		}else{
			forwardAction("/car/order/forwardOrder");
		}
	}
	
	/**
	 * 上班
	 * wash_work_person 设置当前设备的洗车工
	 * wash_work_device_record 记录洗车工的上班记录
	 */
	public void startWork(){
		log.info("洗车工上班");
		JsonResult jsonResult = new JsonResult();
		String mac = getPara("device_mac");
		String memberId = (String)getSessionAttr("memberIdSession");
		//WashWorkPerson washWorkPerson = WashWorkPerson.dao.findByIdLoadColumns(new Object[]{mac}, "open_id");
		WashDevice washDevice = WashDevice.dao.findFirst("select * from wash_device WHERE mac = ? ", mac);
		if(null != washDevice){
			//设备是否在线
			if(null != washDevice.getStatus() && Consts.DeviceStatus_0.equals(washDevice.getStatus())){
				WorkService workService = enhance(WorkService.class);
				workService.startWork(washDevice, memberId);
				
				jsonResult.setRtnCode(0);
				jsonResult.setRtnMsg("操作成功");
			}else{
				jsonResult.setRtnCode(1);
				jsonResult.setRtnMsg("设备故障或离线，请联系管理员");
			}
		}else{
			jsonResult.setRtnCode(2);
			jsonResult.setRtnMsg("设备不存在，请联系管理员");
		}
		
		renderJson(jsonResult);
	}
	
	/**
	 * 下班
	 */
	public void endWork(){
		log.info("洗车工下班");
		JsonResult jsonResult = new JsonResult();
		String mac = getPara("device_mac");
		String memberId = (String)getSessionAttr("memberIdSession");
		//WashWorkPerson washWorkPerson = WashWorkPerson.dao.findByIdLoadColumns(new Object[]{mac}, "open_id");
		WashDevice washDevice = WashDevice.dao.findFirst("select * from wash_device WHERE mac = ? ", mac);
		if(null != washDevice){
			//设备是否在线
			if(null != washDevice.getStatus() && Consts.DeviceStatus_0.equals(washDevice.getStatus())){
				WorkService.endWork(washDevice, memberId);
				
				jsonResult.setRtnCode(0);
				jsonResult.setRtnMsg("操作成功");
			}else{
				jsonResult.setRtnCode(1);
				jsonResult.setRtnMsg("设备故障或离线，请联系管理员");
			}
		}else{
			jsonResult.setRtnCode(2);
			jsonResult.setRtnMsg("设备不存在，请联系管理员");
		}
		
		renderJson(jsonResult);
	}
}
