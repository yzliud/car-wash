package com.wash.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.helper.StringUtil;

import com.wash.model.WashCouponDetail;
import com.wash.model.WashPlateNumber;
import com.wash.model.WashSetMeal;

public class OrderService {
	
	public static Map<String, Object> reckonFee(String carNumber, String setMealId, String couponId){
		
		//判断是否是内部车牌
		WashPlateNumber washPlateNumber = null;
		if(!StringUtil.isBlank(carNumber)){
			washPlateNumber = WashPlateNumber.dao.findFirst("select * from wash_plate_number where car_number = ? ", carNumber);
		}
		//取套餐金额
		WashSetMeal wsm = null;
		if(!StringUtil.isBlank(setMealId)){
			wsm = WashSetMeal.dao.findById(setMealId);
		}else{
			wsm = WashSetMeal.dao.findFirst(" select * from wash_set_meal order by update_time desc ");
		}
		
		BigDecimal totalFee = BigDecimal.ZERO;
		BigDecimal discountFee = BigDecimal.ZERO;
		BigDecimal realFee = BigDecimal.ZERO;
		if(null == washPlateNumber){
			totalFee = wsm.getSalePrice();
		}else{
			//取内部价
			totalFee = washPlateNumber.getPrice();
		}
		
		WashCouponDetail washCouponDetail = null;
		if(!StringUtil.isBlank(couponId)){
			washCouponDetail = WashCouponDetail.dao.findFirst(
					"select * from wash_coupon_detail where id = ? and status = 1 and now() between effective_time and DATE_ADD(failure_time, INTERVAL 1 DAY)  "
					, couponId); 
			if(washCouponDetail != null){
				discountFee = washCouponDetail.getDiscountAmount();
			}
		}
		
		realFee = totalFee.subtract(discountFee);
		
		//金额小于0的时候 
		if(realFee.compareTo(BigDecimal.ZERO) == -1){
			realFee = BigDecimal.ZERO;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalFee", totalFee);
		map.put("discountFee", discountFee);
		map.put("realFee", realFee);
		map.put("washSetMeal", wsm);
		map.put("washCouponDetail", washCouponDetail);
		return map;
	}

}
