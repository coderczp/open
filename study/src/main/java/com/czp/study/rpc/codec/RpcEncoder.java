package com.czp.study.rpc.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferFactory;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelConfig;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * Function:XXX<br>
 *
 * Date :2015年12月27日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class RpcEncoder extends OneToOneEncoder {

	private static final byte[] EMPTY = new byte[0];

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel ch, Object msg)
			throws Exception {
		ChannelConfig config = ctx.getChannel().getConfig();
		ChannelBufferFactory factory = config.getBufferFactory();
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer(factory);
		if (msg instanceof RpcRequest) {
			buf.writeByte(RpcDecoder.REQ);
			RpcRequest req = (RpcRequest) msg;
			writeWithLength(buf, req.getExportName().getBytes());
			writeWithLength(buf, req.getMethodName().getBytes());
			writeHeaderArray(buf, req.getArgs());
			return buf;
		} else if (msg instanceof RpcResponse) {
			buf.writeByte(RpcDecoder.RSP);
			RpcResponse rsp = (RpcResponse) msg;
			writeHeaderArray(buf, rsp.getResult());
			String err = rsp.getError();
			byte[] bytes = (err == null) ? EMPTY : err.getBytes();
			writeWithLength(buf, bytes);
			return buf;
		}
		throw new RuntimeException("unsupport type:" + msg);
	}

	private void writeWithLength(ChannelBuffer buf, byte[] bs) {
		buf.writeByte(bs.length);
		buf.writeBytes(bs);
	}

	private void writeHeaderArray(ChannelBuffer buf, byte[][] bs) {
		if (bs == null) {
			buf.writeByte(0);
			return;
		}
		buf.writeByte(bs.length);
		for (byte[] temp : bs) {
			writeWithLength(buf, temp);
		}
	}

}
