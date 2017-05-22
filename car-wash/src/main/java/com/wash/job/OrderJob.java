package com.wash.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;

public class OrderJob implements Job {
	
	private static Log log = Log.getLog(OrderJob.class);

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~报警定时任务执行~~~~~~~~~~~~~~~~~~~~~~~~~~");
		String sql = " UPDATE  wash_ord_order SET order_status = 9, update_date = now()   WHERE order_status = 3 AND end_time <= DATE_SUB(NOW(),INTERVAL 1 DAY); "
				+ " INSERT INTO wash_ord_evaluate "
				+ " ( id, flag, evaluate, STATUS, create_date, update_date, del_flag ) "
				+ "  SELECT id,'0','系统默认好评','0',NOW(),NOW(),0 FROM wash_ord_order WHERE order_status = 3 AND end_time <= DATE_SUB(NOW(),INTERVAL 1 DAY);";
		Db.update(sql);
		log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~报警定时任务执行完成~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

}
