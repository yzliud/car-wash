package com.wash.controller;

import org.jsoup.helper.StringUtil;

import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.samehope.core.render.JsonResult;
import com.wash.Consts;
import com.wash.model.WashCouponDetail;

public class CouponController extends Controller{
	
	private static Log log = Log.getLog(CouponController.class);
	
	/**
	 * 页面跳转
	 */
	public void forward(){
		render("userCoupon.html");
	}
	
	/**
	 * 页面跳转
	 */
	public void forwardList(){
		render("userCouponList.html");
	}
	
	/**
	 * 录入优惠券
	 */
	public void receiveCoupon(){
		String memberId = getSessionAttr("memberIdSession");
		String couponNo = getPara("couponNo");
		log.debug("优惠券"+couponNo+"-------"+memberId);
		JsonResult jsonResult = new JsonResult();
		if(!StringUtil.isBlank(couponNo)){
			WashCouponDetail washCouponDetail = WashCouponDetail.dao.findFirst(
					"select * from wash_coupon_detail where coupon_no = ? "
					, couponNo);
			if(null != washCouponDetail){
				if(!StringUtil.isBlank(washCouponDetail.getStatus()) && washCouponDetail.getStatus().equals(Consts.CouponStatus_0)){
					int count = Db.update("update wash_coupon_detail set member_id = ?, status = 1, update_date = now() where coupon_no = ? and status = 0 "
							, memberId, couponNo);
					if(count > 0){
						jsonResult.setRtnCode(0);
						jsonResult.setRtnMsg("操作成功");
					}else{
						jsonResult.setRtnCode(1);
						jsonResult.setRtnMsg("优惠券已被领取");
					}
				}else{
					jsonResult.setRtnCode(2);
					jsonResult.setRtnMsg("优惠券已被领取");
				}
			}else{
				jsonResult.setRtnCode(3);
				jsonResult.setRtnMsg("该优惠券不存在，请重新输入");
			}
		}
		log.debug(jsonResult.toString());
		renderJson(jsonResult);
	}
	
	/**
	 * 优惠券列表
	 */
	public void couponList(){
		String memberId = getSessionAttr("memberIdSession");
		String counStatus = getPara("counStatus");
		int pageNumber = getParaToInt("pageNumber");
		log.debug("优惠券列表");
		String from = "";
		String select = "select * ";
		Page<WashCouponDetail> wcdList = null;
		if(!StringUtil.isBlank(counStatus)){
			if(counStatus.equals(Consts.CouponStatus_1)){
				from = " from wash_coupon_detail where member_id = ? and status = 1 and failure_time > now() ";
				wcdList = WashCouponDetail.dao.paginate(pageNumber, Consts.PageSize, select, from, memberId);
			}else if(counStatus.equals(Consts.CouponStatus_2)){
				from = " from wash_coupon_detail where member_id = ? and status = 2 ";
				wcdList = WashCouponDetail.dao.paginate(pageNumber, Consts.PageSize, select, from, memberId);
			}else if(counStatus.equals(Consts.CouponStatus_9)){
				from = " from wash_coupon_detail where member_id = ? and status = 1 and now() > failure_time ";
				wcdList = WashCouponDetail.dao.paginate(pageNumber, Consts.PageSize, select, from, memberId);
			}
		}else{
			from = " from wash_coupon_detail where member_id = ? ";
			wcdList = WashCouponDetail.dao.paginate(pageNumber, Consts.PageSize, select, from, memberId);
		}
		renderJson(wcdList);
	}
}
