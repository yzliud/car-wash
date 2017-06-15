package com.wash.controller;

import io.netty.channel.Channel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jsoup.helper.StringUtil;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.samehope.common.utils.UuidUtils;
import com.samehope.core.render.JsonResult;
import com.samehope.plugin.wechat.WechatKit;
import com.samehope.plugin.wechat.jspay.WechatPay;
import com.samehope.plugin.wechat.model.WechatConfig;
import com.wash.Consts;
import com.wash.Global;
import com.wash.model.WashCompanyPurse;
import com.wash.model.WashCouponDetail;
import com.wash.model.WashDevice;
import com.wash.model.WashMember;
import com.wash.model.WashOrdEvaluate;
import com.wash.model.WashOrdOrder;
import com.wash.model.WashSetMeal;
import com.wash.model.WashTFlow;
import com.wash.model.WashWorkPerson;
import com.wash.service.OrderService;

public class OrderController extends Controller{

	private static Log log = Log.getLog(OrderController.class);
	
	/**
	 * 跳转到订单页面
	 */
	public void forwardOrder(){
		log.info("跳转");
		//WashOrdOrder woo = new WashOrdOrder();
		String mac = getPara("device_mac");
		WashMember washMember = (WashMember)getSessionAttr("memberDataSession");
		if(washMember!=null){
			//首次进入需要绑定手机号码
			if(StringUtil.isBlank(washMember.getMobile())){
				setAttr("device_mac",mac);
				render("bind.html");
			}else{
				setAttr("washMember", washMember);
				
				Map<String, Object> mapFee = OrderService.reckonFee(washMember.getCardNum(), "", "");
				
				BigDecimal totalFee = (BigDecimal)mapFee.get("totalFee");
				//BigDecimal discountFee = (BigDecimal)mapFee.get("discountFee");
				BigDecimal realFee = (BigDecimal)mapFee.get("realFee");
				WashSetMeal wsm = (WashSetMeal)mapFee.get("washSetMeal");
				
				//优惠卷
				List<WashCouponDetail> wcdList = WashCouponDetail.dao.find(
						"select * from wash_coupon_detail where member_id = ? and status = 1 and now() between effective_time and failure_time ORDER BY discount_amount DESC,failure_time "
						, washMember.getId()); 	
				setAttr("totalFee", totalFee);
				setAttr("realFee", realFee);
				setAttr("wcdList", wcdList);
				setAttr("washSetMealData", wsm);
				setAttr("device_mac",mac);
				render("order.html");
			}
		}
	}
	
	/**
	 * 计算价格
	 */
	public void reckon(){
		String carNumber = getPara("car_number");
		String setMealId = getPara("set_meal_id");
		String couponId = getPara("coupon_id");
		//判断是否是内部车牌
		Map<String, Object> map = OrderService.reckonFee(carNumber, setMealId, couponId);
		
		renderJson(map);
	}
	
	/**
	 * 跳转支付
	 */
	public void pay(){
		log.info("支付");
		String setMealId = getPara("set_meal_id");
		String cardNum = getPara("car_number");
		String couponId = getPara("coupon_id");
		String mobile = getPara("mobile");
		String deviceMac = getPara("device_mac");
		
		String memberId = (String)getSessionAttr("memberIdSession");
		String openId = (String)getSessionAttr("openIdSession");
		
		//设备信息
		WashDevice washDevice = WashDevice.dao.findFirst("select * from wash_device WHERE mac = ? ", deviceMac);
		String deviceId = washDevice.getId();
		
		//更改绑定车牌
		WashMember wm = WashMember.dao.findFirst("select * from wash_member WHERE open_id = ? ", openId);
		wm.setCardNum(cardNum);
		wm.update();
		setSessionAttr("memberDataSession", wm);

		//获取金额
		Map<String, Object> mapFee = OrderService.reckonFee(cardNum, setMealId, couponId);
		BigDecimal totalFee = (BigDecimal)mapFee.get("totalFee");
		BigDecimal discountFee = (BigDecimal)mapFee.get("discountFee");
		BigDecimal realFee = (BigDecimal)mapFee.get("realFee");
		WashCouponDetail washCouponDetail = (WashCouponDetail)mapFee.get("washCouponDetail");
		
		
		Date date = new Date();
		//订单编号
		String nowDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		Random random = new Random();
		int x = random.nextInt(89999999);
		x = x + 10000000;
		String orderNo = nowDate + x;
		
		//订单
		WashOrdOrder woo = new WashOrdOrder();
		woo.setId(UuidUtils.getUuid());
		woo.setOrderNo(orderNo);
		woo.setCarPersonId(memberId);
		woo.setDeviceId(deviceId);
		woo.setDeviceMac(deviceMac);
		woo.setOrderTime(date);
		woo.setMobile(mobile);
		woo.setCarNumber(cardNum);
		woo.setOrderStatus(Consts.orderStatus_0);
		woo.setPayMode(Consts.payMode_0);
		woo.setSetMealId(setMealId);
		woo.setTotalFee(totalFee);
		woo.setDiscountFee(discountFee);
		woo.setRealFee(realFee);
		woo.setWashFee(new BigDecimal(0));
		woo.setCreateDate(date);
		woo.setUpdateDate(date);
		woo.setDelFlag("0");
		boolean flag = false;
		//判读是否需要支付金额
		if(realFee.compareTo(BigDecimal.ZERO) == 1){
			flag = woo.save();
			if(flag){
				//更新优惠价使用信息
				if(washCouponDetail != null){
					washCouponDetail.setOrderNo(orderNo);
					washCouponDetail.update();
				}
				//发起支付
				Map<String,String> map = new HashMap<String,String>();
				map.put("orderId", orderNo);
				map.put("payMoney", realFee.doubleValue()+"");
				map.put("controllerKey", "/car/order");
				map.put("methodName", "forward");
				WechatConfig wechatConfig = WechatConfig.getWechatConfig(PropKit.use("wx_config.properties").get("appid")
						, PropKit.use("wx_config.properties").get("secret")
						, PropKit.use("wx_config.properties").get("partner")
						, PropKit.use("wx_config.properties").get("partnerkey")
						, openId);
				WechatKit.toJspayOuth2(wechatConfig, map, getRequest(), getResponse());
			}
		}else{
			woo.setRealFee(BigDecimal.ZERO);
			woo.setPayTime(date);
			woo.setOrderStatus(Consts.orderStatus_1);
			flag = woo.save();
			if(washCouponDetail != null){
				washCouponDetail.setOrderNo(orderNo);
				washCouponDetail.setStatus(Consts.CouponStatus_2);
				washCouponDetail.update();
			}
			forwardAction("/car/order/forward");
		}
		
		
	}
	
	/**
	 * 订单存在，发起支付
	 */
	public void topay(){
		log.info("支付");
		String orderId = getPara("order_id");
		//查询订单
		WashOrdOrder woo = WashOrdOrder.dao.findFirst(" select * from wash_ord_order where id = ? ", orderId);
		String openId = (String)getSessionAttr("openIdSession");
		if(woo != null){
			Map<String,String> map = new HashMap<String,String>();
			map.put("orderId", woo.getOrderNo());
			map.put("payMoney", woo.getRealFee()+"");
			map.put("controllerKey", "/car/order");
			map.put("methodName", "forward");
			WechatConfig wechatConfig = WechatConfig.getWechatConfig(PropKit.use("wx_config.properties").get("appid")
					, PropKit.use("wx_config.properties").get("secret")
					, PropKit.use("wx_config.properties").get("partner")
					, PropKit.use("wx_config.properties").get("partnerkey")
					, openId);
			WechatKit.toJspayOuth2(wechatConfig, map, getRequest(), getResponse());
		}
	}
	
	/**
	 * 支付回调
	 */
	@Clear
	public void payBack(){
		log.info("支付回调");
		
//		Map<String, String> checkPayResult = WechatPay.checkPayResult(getRequest());
//		String rtnCode = checkPayResult.get("rtnCode");
//		String rtnMsg = checkPayResult.get("rtnMsg");
//		String transactionId = checkPayResult.get("transactionId");
//		String orderId = checkPayResult.get("orderId");
		String rtnCode = getPara("rtnCode");
		String rtnMsg = getPara("rtnMsg");
		String transactionId = getPara("transactionId");
		String orderId = getPara("orderId");
		log.info("rtnCode===="+rtnCode);
		log.info("rtnMsg===="+rtnMsg);
		//验签通过
		if("0".equals(rtnCode)&&"success".equals(rtnMsg)){
			Date date = new Date();
			//查询订单信息
			WashOrdOrder woo = WashOrdOrder.dao.findFirst("select * from wash_ord_order where order_no = ? and del_flag = 0 ", orderId);
			if(woo != null ){
				boolean rtnFlag = true;
				//订单为未支付， 才进行处理
				if(Consts.orderStatus_0.equals(woo.getOrderStatus())){
					woo.setOrderStatus(Consts.orderStatus_1);
					woo.setPayTime(date);
					woo.setPaySerialNumber(transactionId);
					woo.setUpdateDate(date);
					rtnFlag = woo.update();
					
					int flowId = Db.queryInt(" select _nextval('flow_sn') ");
					
					//流水
					WashTFlow washTFlow = new WashTFlow();
					washTFlow.setId(flowId+"");
					washTFlow.setTSn(woo.getOrderNo());
					washTFlow.setTType("00");
					washTFlow.setMemberId(woo.getCarPersonId());
					washTFlow.setTppType(0);
					washTFlow.setTAmount(woo.getRealFee());
					washTFlow.setTppSn(woo.getPaySerialNumber());
					washTFlow.save();
					
					WashCompanyPurse washCompanyPurse = WashCompanyPurse.dao.findFirst("select * from wash_company_purse order by t_datetime desc ");
					BigDecimal balance = new BigDecimal(0);
					if(null != washCompanyPurse){
						balance = washCompanyPurse.getBalance();
					}
					BigDecimal income = woo.getRealFee();
					WashCompanyPurse wcp = new WashCompanyPurse();
					wcp.setId(UuidUtils.getUuid());
					wcp.setUid(woo.getCarPersonId());
					wcp.setTFlowNo(flowId+"");
					wcp.setTType("0");
					wcp.setTDatetime(new Date());
					wcp.setIncome(income);
					wcp.setPay(new BigDecimal(0));
					wcp.setBalance(balance.add(income));
					//更新优惠卷使用信息
					Db.update("update wash_coupon_detail set status = 2, update_date = now() where order_no = ? ", orderId);
				}
				if(rtnFlag){
					log.info("向微信服务器告知支付成功!!!!!!");
					WechatPay.responWXResult("success", getResponse());
				}
			}
		}else{
			log.info("向微信服务器告知支付失败!!!!!!");
			WechatPay.responWXResult("false", getResponse());
		}
		renderNull();
	}
	
	/**
	 * 查询订单历史信息
	 */
	public void orderHis(){
		log.info("订单查询");
		int pageNumber = getParaToInt("pageNumber");
		String memberId = (String)getSessionAttr("memberIdSession");
		String select = " ";
		String from = " ";
		Page<Record> recordList = null;
		
		select = " SELECT a.id,a.order_no,a.real_fee,a.order_time,a.pay_time,a.end_time,b.name device_name,b.address device_address,c.nick_name,d.flag evaluate_flag,d.status evaluate_status,d.evaluate,d.add_evaluate,a.order_status "
				+" ,CASE a.order_status WHEN 0 THEN '待付款'  WHEN 1 THEN '等待洗车' WHEN 2 THEN  '洗车中' WHEN 3 THEN  '待评价' WHEN 9 THEN  '已完结' ELSE '' END order_status_value "
				+" ,CASE d.flag WHEN 0 THEN '好评'  WHEN 1 THEN '中评' WHEN 2 THEN  '差评' ELSE '' END evaluate_flag_value "
				;
		from =  " FROM wash_ord_order a"
				+" LEFT JOIN wash_device b ON a.device_id = b.id"
				+" LEFT JOIN wash_member c ON a.wash_person_id = c.id"
				+" LEFT JOIN wash_ord_evaluate d ON a.id = d.id"
				+" WHERE  a.del_flag = 0 and car_person_id = ? and a.order_status != 0 order by a.update_date desc ";
		recordList = Db.paginate(pageNumber, Consts.PageSize, select, from, memberId);
		
		renderJson(recordList);
	}
	
	/**
	 * 订单页面跳转
	 */
	public void forward(){
		
		render("orderHis.html");
	}
	
	/**
	 * 跳转到评价页面
	 */
	public void forwardAppraise(){
		String orderId = getPara("order_id");
		setAttr("order_id",orderId);
		WashOrdEvaluate washOrdEvaluate = WashOrdEvaluate.dao.findById(orderId);
		//判断是二次评价 还是首次评价
		if(washOrdEvaluate == null){
			render("appraise.html");
		}else{
			render("appraiseAdd.html");
		}
	}
	
	/**
	 * 评价信息保存
	 */
	public void appraise(){
		String evaluate = getPara("evaluate");
		String orderId = getPara("orderId");
		String flag = getPara("flag");
		Date date = new Date();
		JsonResult jsonResult = new JsonResult();
		if(!StringUtil.isBlank(orderId)){
			WashOrdEvaluate washOrdEvaluate = WashOrdEvaluate.dao.findById(orderId);
			boolean ispass = false;
			//首次评价还是追加评论
			if(null == washOrdEvaluate){
				washOrdEvaluate = new WashOrdEvaluate();
				washOrdEvaluate.setId(orderId);
				washOrdEvaluate.setFlag(flag);
				washOrdEvaluate.setStatus(Consts.evaluateStatus_0);
				washOrdEvaluate.setEvaluate(evaluate);
				washOrdEvaluate.setCreateDate(date);
				washOrdEvaluate.setUpdateDate(date);
				washOrdEvaluate.setDelFlag("0");
				ispass = washOrdEvaluate.save();
			}else{
				washOrdEvaluate.setAddEvaluate(evaluate);
				washOrdEvaluate.setStatus(Consts.evaluateStatus_1);
				washOrdEvaluate.setUpdateDate(date);
				ispass = washOrdEvaluate.update();
			}
			WashOrdOrder washOrdOrder = WashOrdOrder.dao.findById(orderId);
			washOrdOrder.setOrderStatus(Consts.orderStatus_9);
			washOrdOrder.setUpdateDate(date);
			washOrdOrder.update();
			if(ispass){
				jsonResult.setRtnCode(0);
				jsonResult.setRtnMsg("评价成功");
			}else{
				jsonResult.setRtnCode(1);
				jsonResult.setRtnMsg("操作失败，请待会重新提交");
			}
		}else{
			jsonResult.setRtnCode(2);
			jsonResult.setRtnMsg("请选择订单");
		}
		renderJson(jsonResult);
	}
	
	/**
	 * 删除订单
	 */
	public void del(){
		log.info("删除订单");
		JsonResult jsonResult = new JsonResult();
		String orderId = getPara("order_id");
		WashOrdOrder woo = WashOrdOrder.dao.findFirst(" select * from wash_ord_order where id = ? ", orderId);
		if(woo != null){
			woo.setDelFlag(Consts.DelFlag_1);
			woo.setUpdateDate(new Date());
			boolean ispass = woo.update();
			if(ispass){
				jsonResult.setRtnCode(0);
				jsonResult.setRtnMsg("删除成功");
			}else{
				jsonResult.setRtnCode(1);
				jsonResult.setRtnMsg("此订单不存在");
			}
		}else{
			jsonResult.setRtnCode(2);
			jsonResult.setRtnMsg("请选择订单");
		}
		renderJson(jsonResult);
	}
	
	/**
	 * 根据设备MAC 进行支付前置判断
	 */
	public void check(){
		log.info("支付前置判断");
		JsonResult jsonResult = new JsonResult();
		String deviceMac = getPara("device_mac");
		WashDevice wd = WashDevice.dao.findFirst(" select * from wash_device where mac = ? ", deviceMac);
		
		if(wd == null){
			jsonResult.setRtnCode(1);
			jsonResult.setRtnMsg("设备维护中");
		}else{
			if(wd.getStatus().equals(Consts.DeviceStatus_1) || wd.getStatus().equals(Consts.DeviceStatus_2)){
				jsonResult.setRtnCode(2);
				jsonResult.setRtnMsg("设备维护中");
			}else{
				WashWorkPerson wwp = WashWorkPerson.dao.findById(wd.getId());
				if(null == wwp || StringUtil.isBlank(wwp.getWashPersonId())){
					jsonResult.setRtnCode(3);
					jsonResult.setRtnMsg("洗车工没有上班，请稍后");
				}else{
					Channel deviceChannel = Global.deviceMap.get(deviceMac);
					if(null == deviceChannel){
						jsonResult.setRtnCode(4);
						jsonResult.setRtnMsg("设备维护中");
					}else{
						jsonResult.setRtnCode(0);
						jsonResult.setRtnMsg("操作成功");
					}
				}
			}
		}
		renderJson(jsonResult);
	}
}
