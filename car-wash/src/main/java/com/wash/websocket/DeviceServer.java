package com.wash.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import com.jfinal.log.Log;
import com.wash.websocket.ByteDecoder;
import com.wash.Global;

public class DeviceServer {
	
	private static Log log = Log.getLog(DeviceServer.class);
	
	private int port;

	public DeviceServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {

		log.info("设备WEBSOCKET启动");
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {

					ChannelPipeline p = ch.pipeline();
					p.addLast(new ByteDecoder());
					p.addLast(new StringEncoder(CharsetUtil.UTF_8));
					p.addLast(new StringDecoder(CharsetUtil.UTF_8)); 
					p.addLast(new DeviceServerHandler());
				}
			});
			Global.EventLoopGroup_device.put("bossGroup", bossGroup);
			Global.EventLoopGroup_device.put("workerGroup", workerGroup);
			
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {

		new DeviceServer(8008).run();
	}

}
