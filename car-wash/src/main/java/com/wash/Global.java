package com.wash;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

import java.util.HashMap;
import java.util.Map;

public class Global {
	
	//设备grop,用于关闭设备连接
	public static Map<String, EventLoopGroup> EventLoopGroup_device = new HashMap<String, EventLoopGroup>();
	//设备ID与设备通道的map
	public static Map<String, Channel> deviceMap = new HashMap<String, Channel>();
	
	//通道ID与设备MAC的map
	public static Map<String, String> channelMap = new HashMap<String, String>();


}
