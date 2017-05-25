package com.wash.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.plugin.activerecord.Db;

public class HeartJob implements Job {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		String warning = "INSERT INTO wash_device_fault(id, mac, device_name, device_address, STATUS, fault_time, fault_desc, create_date, update_date, del_flag) "
					+" SELECT REPLACE (UUID(), '-', ''), a.mac,a.name,a.address,0,NOW(),'设备目前无心跳',NOW(),NOW(),0 FROM wash_device a WHERE " 
					+" NOT EXISTS(SELECT 1 FROM wash_device_heart b WHERE a.mac = b.mac AND b.operator_time >= DATE_SUB(NOW(), INTERVAL 10 MINUTE)) "
					+" AND STATUS = 0";
		Db.update(warning);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~报警定时任务执行完成~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
}
