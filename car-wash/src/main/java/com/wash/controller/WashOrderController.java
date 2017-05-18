package com.wash.controller;

import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wash.Consts;
import com.wash.model.WashWorkPerson;

/**
 * 洗车工
 * @author Administrator
 *
 */
public class WashOrderController extends Controller{

	private static Log log = Log.getLog(WashOrderController.class);
	
	/**
	 * 当前订单
	 */
	public void orderWork(){
		log.info("订单查询");
		int pageNumber = getParaToInt("pageNumber");
		String memberId = (String)getSessionAttr("memberIdSession");
		
		String deviceId = "";
		//MAC 为空时 查询工作表
		WashWorkPerson washWorkPerson = WashWorkPerson.dao.findFirst("SELECT * FROM wash_work_person WHERE wash_person_id = ? ", memberId);
		if(washWorkPerson != null && washWorkPerson.getId() != null){
			deviceId = washWorkPerson.getId();
		}
		
		String select = " ";
		String from = " ";
		Page<Record> recordList = null;

		select = " SELECT a.id,a.order_no,a.real_fee,a.device_id,a.order_time,a.pay_time,a.end_time,b.name device_name,c.nick_name,c.mobile,c.car_number,d.flag evaluate_flag,d.evaluate,d.add_evaluate,a.order_status "
				+" ,CASE a.order_status WHEN 0 THEN '待付款'  WHEN 1 THEN '等待洗车' WHEN 2 THEN  '洗车中' WHEN 3 THEN  '待评价' WHEN 9 THEN  '已完结' ELSE '' END order_status_value "
				+" ,CASE d.flag WHEN 0 THEN '好评'  WHEN 1 THEN '中评' WHEN 2 THEN  '差评' ELSE '' END evaluate_flag_value "
				;
		from =  " FROM wash_ord_order a"
				+" LEFT JOIN wash_device b ON a.device_id = b.id "
				+" LEFT JOIN wash_member c ON a.car_person_id = c.id "
				+" LEFT JOIN wash_ord_evaluate d ON a.id = d.id "
				+" WHERE  a.del_flag = 0 and a.device_id = ? "
				+ " and a.order_status in(1,2) order by pay_time ";
		
		recordList = Db.paginate(pageNumber, Consts.PageSize,  select, from, deviceId);

		renderJson(recordList);
	}
	
	/**
	 * 查询历史订单
	 */
	public void orderHis(){
		log.info("查询历史订单");
		int pageNumber = getParaToInt("pageNumber");
		String memberId = (String)getSessionAttr("memberIdSession");
		
		String select = " ";
		String from = " ";
		Page<Record> recordList = null;

		select = " SELECT a.id,a.order_no,a.real_fee,a.device_id,a.order_time,a.pay_time,a.end_time,b.name device_name,c.nick_name,c.mobile,c.car_number,d.flag evaluate_flag,d.status evaluate_status,d.evaluate,d.add_evaluate,a.order_status "
				+" ,CASE a.order_status WHEN 0 THEN '待付款'  WHEN 1 THEN '等待洗车' WHEN 2 THEN  '洗车中' WHEN 3 THEN  '待评价' WHEN 9 THEN  '已完结' ELSE '' END order_status_value "
				+" ,CASE d.flag WHEN 0 THEN '好评'  WHEN 1 THEN '中评' WHEN 2 THEN  '差评' ELSE '' END evaluate_flag_value "
				;
		from =  " FROM wash_ord_order a"
				+" LEFT JOIN wash_device b ON a.device_id = b.id "
				+" LEFT JOIN wash_member c ON a.car_person_id = c.id "
				+" LEFT JOIN wash_ord_evaluate d ON a.id = d.id "
				+" WHERE  a.del_flag = 0 and wash_person_id = ? "
				+ " order by a.update_date desc ";
		recordList = Db.paginate(pageNumber, Consts.PageSize,  select, from, memberId);

		renderJson(recordList);
	}
	
	public void forwardWork(){
		
		render("orderWork.html");
	}
	
	public void forwardHis(){
		
		render("orderWashHis.html");
	}
}
