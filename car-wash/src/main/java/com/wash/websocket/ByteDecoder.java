package com.wash.websocket;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ByteDecoder extends ByteToMessageDecoder {
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		byte n = "n".getBytes()[0];
		byte p = in.readByte();
		in.resetReaderIndex();
		if (n != p) {
			// 把读取的起始位置重置
			ByteBufToBytes reader = new ByteBufToBytes();
			byte[] t = reader.read(in);
			out.add(bytesToHexString(t));

			//for (int i = 0; i < in.capacity(); i++) {
			//	byte b = in.getByte(i);
			//	System.out.println("byte:" + b);
			//}
		} else {
			// 执行其它的decode
			ctx.fireChannelRead(in);
		}
	}
	
	public static String bytesToHexString(byte[] bArray) {   
	    StringBuffer sb = new StringBuffer(bArray.length);   
	    String sTemp;   
	    for (int i = 0; i < bArray.length; i++) {   
	     sTemp = Integer.toHexString(0xFF & bArray[i]);   
	     if (sTemp.length() < 2)   
	      sb.append(0);   
	     sb.append(sTemp.toUpperCase());   
	    }   
	    return sb.toString();   
	}  

}