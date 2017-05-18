package com.wash;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Global {
	
	//设备grop,用于关闭设备连接
	public static Map<String, EventLoopGroup> EventLoopGroup_device = new HashMap<String, EventLoopGroup>();
	//设备ID与设备通道的map
	public static Map<String, Channel> deviceMap = new HashMap<String, Channel>();

	
	//用于关闭netty
	public static Map<String, EventLoopGroup> EventLoopGroup_client = new HashMap<String, EventLoopGroup>();
	public static Map<String, EventLoopGroup> EventLoopGroup_api = new HashMap<String, EventLoopGroup>();









	

	// L开锁，R红，G绿，B蓝，P蜂鸣器
	public static final String UNLOCK = "L";//4C-L
	public static final String BUZZER = "P";//50-P
	public static final String RED = "R";//52-R
	public static final String GREEN = "G";//47-G
	public static final String BLUE = "B";//42-B
	public static final Object HEX_C = "34";
	public static final String HEX_UNLOCK = "4C";//4C-L
	public static final String HEX_BUZZER = "50";//50-P
	public static final String HEX_RED = "52";//52-R
	public static final String HEX_GREEN = "47";//47-G
	public static final String HEX_BLUE = "42";//42-B
	public static final String SPACE = "\n";
	
	//根据mac查询设备心跳
	public static final String getHeartByMac = "select * from m_device_heart where device_mac = ?";
	
	//根据mac查找设备
	public static final String getDeviceByMac = "select * from m_device where mac = ?";

}
