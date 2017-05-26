package com.wash.service;

import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.samehope.common.utils.UuidUtils;
import com.wash.Consts;
import com.wash.model.WashDevice;
import com.wash.model.WashWorkDeviceRecord;
import com.wash.model.WashWorkPerson;

public class WorkService {
	
	@Before(Tx.class)
	public boolean startWork(WashDevice washDevice,String memberId){
		
		Date date = new Date();
		
		//判断是否上班
		boolean flag = false;
		
		boolean flag_1 = true;
		boolean flag_2 = true;
		boolean flag_3 = true;
		
		//将其他设备的上班记录关闭，并记录下班记录
		Db.update("update wash_work_person set wash_person_id = '' where wash_person_id = ? and id != ? ", memberId, washDevice.getId());
		Db.update("update wash_work_device_record set work_off_time = ?, flag = 1, update_date = ? where wash_person_id = ? and wash_device_id != ? and flag = 0 ",
				date, date, memberId, washDevice.getId());
		
		WashWorkPerson washWorkPerson =  WashWorkPerson.dao.findById(washDevice.getId());
		if(washWorkPerson != null && washWorkPerson.getId() != null){
			washWorkPerson.setWashDeviceMac(washDevice.getMac());
			washWorkPerson.setWashPersonId(memberId);
			washWorkPerson.setUpdateDate(date);
			washWorkPerson.update();
		}else{
			washWorkPerson = new WashWorkPerson();
			washWorkPerson.setId(washDevice.getId());
			washWorkPerson.setWashDeviceMac(washDevice.getMac());
			washWorkPerson.setWashPersonId(memberId);
			washWorkPerson.setCreateDate(date);
			washWorkPerson.setUpdateDate(date);
			washWorkPerson.setDelFlag("0");
			washWorkPerson.save();
		}
		
		//获取设备最新一条记录
		WashWorkDeviceRecord lastWdRecord = WashWorkDeviceRecord.dao.findFirst("select * from wash_work_device_record where wash_device_mac = ? order by update_date desc", washDevice.getMac());
		if( null != lastWdRecord && Consts.DeviceWorkFlag_0.equals(lastWdRecord.getFlag())){
			
			//将当前设备的工人 下班
			if(!lastWdRecord.getWashPersonId().equals(memberId)){
				lastWdRecord.setWorkOffTime(date);
				lastWdRecord.setUpdateDate(date);
				lastWdRecord.setFlag(Consts.DeviceWorkFlag_1);
				flag_2 = lastWdRecord.update();
				flag = true;
			}
		}else{
			flag = true;
		}
		
		if(flag){
			WashWorkDeviceRecord washWorkDeviceRecord = new WashWorkDeviceRecord();
			washWorkDeviceRecord.setId(UuidUtils.getUuid());
			washWorkDeviceRecord.setWashDeviceId(washDevice.getId());
			washWorkDeviceRecord.setWashDeviceMac(washDevice.getMac());
			washWorkDeviceRecord.setWashPersonId(memberId);
			washWorkDeviceRecord.setFlag(Consts.DeviceWorkFlag_0);
			washWorkDeviceRecord.setWorkOnTime(date);
			washWorkDeviceRecord.setCreateDate(date);
			washWorkDeviceRecord.setUpdateDate(date);
			washWorkDeviceRecord.setDelFlag("0");
			flag_3 = washWorkDeviceRecord.save();
		}
		
		
		return true;
	}
	
	public static boolean endWork(WashDevice washDevice,String memberId){
		
		Date date = new Date();
		
		//判断是否上班
		boolean flag = false;
		
		boolean flag_1 = true;
		boolean flag_2 = true;
		boolean flag_3 = true;
		
		WashWorkPerson washWorkPerson =  WashWorkPerson.dao.findById(washDevice.getId());
		if(washWorkPerson != null && washWorkPerson.getId() != null){
			washWorkPerson.setWashDeviceMac(washDevice.getMac());
			washWorkPerson.setWashPersonId("");
			washWorkPerson.setUpdateDate(date);
			washWorkPerson.update();
		}
		
		//获取设备最新一条记录
		WashWorkDeviceRecord lastWdRecord = WashWorkDeviceRecord.dao.findFirst("select * from wash_work_device_record where wash_device_mac = ? order by update_date desc", washDevice.getMac());
		if( null != lastWdRecord && Consts.DeviceWorkFlag_0.equals(lastWdRecord.getFlag())){
			
			if(lastWdRecord.getWashPersonId().equals(memberId)){
				lastWdRecord.setWorkOffTime(date);
				lastWdRecord.setUpdateDate(date);
				lastWdRecord.setFlag(Consts.DeviceWorkFlag_1);
				flag_2 = lastWdRecord.update();
				flag = true;
			}
		}
		
		
		return true;
	}

}
