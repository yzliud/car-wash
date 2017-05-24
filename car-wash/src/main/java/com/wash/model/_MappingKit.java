package com.wash.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		
		arp.addMapping("wash_coupon", "id", WashCoupon.class);
		arp.addMapping("wash_coupon_detail", "id", WashCouponDetail.class);
		arp.addMapping("wash_device", "id", WashDevice.class);
		arp.addMapping("wash_device_fault", "id", WashDeviceFault.class);
		arp.addMapping("wash_device_heart", "id", WashDeviceHeart.class);
		arp.addMapping("wash_device_record", "id", WashDeviceRecord.class);
		arp.addMapping("wash_member", "id", WashMember.class);
		arp.addMapping("wash_ord_evaluate", "id", WashOrdEvaluate.class);
		arp.addMapping("wash_ord_order", "id", WashOrdOrder.class);
		arp.addMapping("wash_ord_set_meal", "id", WashOrdSetMeal.class);
		arp.addMapping("wash_set_meal", "id", WashSetMeal.class);
		arp.addMapping("wash_t_flow", "id", WashTFlow.class);
		arp.addMapping("wash_work_device_record", "id", WashWorkDeviceRecord.class);
		arp.addMapping("wash_work_person", "id", WashWorkPerson.class);
	}
}

