package com.wash;

import io.netty.channel.EventLoopGroup;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import cn.dreampie.quartz.QuartzKey;
import cn.dreampie.quartz.QuartzPlugin;
import cn.dreampie.quartz.job.QuartzCronJob;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.samehope.plugin.wechat.jspay.WechatPay;
import com.samehope.plugin.wechat.oauth2.WechatOauth2Controller;
import com.wash.controller.CarController;
import com.wash.controller.DeviceController;
import com.wash.controller.ErrorController;
import com.wash.controller.OrderController;
import com.wash.controller.WashOrderController;
import com.wash.controller.WorkController;
import com.wash.handler.SysHandler;
import com.wash.interceptor.WebChatOauthInterceptorByIntro;
import com.wash.job.OrderJob;
import com.wash.model._MappingKit;
import com.wash.websocket.DeviceServer;

public class WashConfig extends JFinalConfig {
	
	private static Log log = Log.getLog(WashConfig.class);
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("db_config.txt");
		me.setDevMode(PropKit.getBoolean("devMode", false));
		me.setViewType(ViewType.FREE_MARKER);
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		
		//微信接口
		me.add("/wechat/oauth2", WechatOauth2Controller.class,"");
		me.add("/wechat/pay", WechatPay.class,"/view/pay");
		
		//洗车工
		me.add("/wash/work", WorkController.class,"/view/wash");
		me.add("/wash/device", DeviceController.class,"/view/wash");
		me.add("/wash/order", WashOrderController.class,"/view/wash");
		
		//车主
		me.add("/car/member", CarController.class,"/view/car");
		me.add("/car/order", OrderController.class,"/view/car");
		
		
		me.add("/error", ErrorController.class,"/view");
	}
	
	public void configEngine(Engine me) {
		//me.addSharedFunction("/common/_layout.html");
		//me.addSharedFunction("/common/_paginate.html");
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
		me.add(druidPlugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		// 所有映射在 MappingKit 中自动化搞定
		_MappingKit.mapping(arp);
		me.add(arp);
		
		QuartzPlugin quartz = new QuartzPlugin();
		me.add(quartz);
	}
	
	public static DruidPlugin createDruidPlugin() {
		return new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		me.add(new WebChatOauthInterceptorByIntro());
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		me.add(new SysHandler());
	}
	
	@Override
	public void afterJFinalStart() {
						
		new Thread(new Runnable() { 
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					new DeviceServer(8123).run();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		new QuartzCronJob(new QuartzKey(1, "test", "test"), "0 */10 * * * ?", OrderJob.class).start();
		
		log.info("System Start~!");
	}
	
	@Override
	public void beforeJFinalStop() {
		
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		if (drivers != null) {
			while (drivers.hasMoreElements()) {
				try {
					Driver driver = drivers.nextElement();
					DriverManager.deregisterDriver(driver);
				} catch (Exception e) {
				}
			}
		}
		
		EventLoopGroup bossGroup_device = Global.EventLoopGroup_device.get("bossGroup");
		EventLoopGroup workerGroup_device = Global.EventLoopGroup_device.get("workerGroup");
		if(bossGroup_device!=null && workerGroup_device!=null){
			bossGroup_device.shutdownGracefully();
			workerGroup_device.shutdownGracefully();
			log.info("EventLoopGroup_device...............shutdownGracefully!");
		}
		log.info("System Close~!");

	}
}
