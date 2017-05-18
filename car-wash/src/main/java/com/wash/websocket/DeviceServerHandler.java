package com.wash.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.log.Log;
import com.samehope.common.utils.UuidUtils;
import com.wash.Global;
import com.wash.model.WashDeviceHeart;

public class DeviceServerHandler extends ChannelInboundHandlerAdapter {

	private static Log log = Log.getLog(DeviceServerHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {

		log.info("DeviceServerHandler::::‘MSG’ from test device:" + msg);
		if(msg != null && !msg.equals("")){       	
			//log.info("DeviceServerHandler::::‘SIZE’ of BODY:" + size);
			String body = msg.toString();
			log.info("DeviceServerHandler::::‘BODY’ from device client:" + body);
			//接收心跳	
			log.info("接收心跳...........");
			List<Object> paramsList = new ArrayList<Object>();
			paramsList.add(body);
			//将通道保存
			Global.deviceMap.put(body, ctx.channel());
			//保存心跳信息
			WashDeviceHeart washDeviceHeart = new WashDeviceHeart();
			washDeviceHeart.setId(UuidUtils.getUuid());
			washDeviceHeart.setMac(body);
			washDeviceHeart.setOperatorTime(new Date());
			washDeviceHeart.save();
			log.info("信道写入！信道ID：：："+body + "++信道的值：：：" + ctx.channel());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		//logger.error("server caught exception", cause);
		ctx.close();
	}
}
