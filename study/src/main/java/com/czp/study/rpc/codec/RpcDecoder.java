package com.czp.study.rpc.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

/**
 * Function:编解码RPC消息<br>
 *
 * Date :2015年12月27日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class RpcDecoder extends OneToOneDecoder {

	public static final int REQ = 1;
	public static final int RSP = 0;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel ch, Object msg)
			throws Exception {
		ChannelBuffer buf = (ChannelBuffer) msg;
		int type = buf.readByte();
		if (type == REQ) {
			int itfNameLen = buf.readUnsignedByte();
			byte[] exportNameBys = new byte[itfNameLen];
			buf.readBytes(exportNameBys);
			int methodNameLen = buf.readUnsignedByte();
			byte[] methodNameBys = new byte[methodNameLen];
			buf.readBytes(methodNameBys);
			int argCount = buf.readUnsignedByte();
			byte[][] args = new byte[argCount][];
			for (int i = 0; i < argCount; i++) {
				int argLen = buf.readUnsignedByte();
				byte[] argBys = new byte[argLen];
				buf.readBytes(argBys);
				args[i] = argBys;
			}
			RpcRequest req = new RpcRequest();
			req.setArgs(args);
			req.setExportName(new String(exportNameBys));
			req.setMethodName(new String(methodNameBys));
			return req;
		} else if (type == RSP) {
			RpcResponse resp = new RpcResponse();
			int resCount = buf.readUnsignedByte();
			resp.setVoid(resCount == 0);
			if (resCount > 0) {
				byte[][] res = new byte[resCount][];
				for (int i = 0; i < resCount; i++) {
					int resBsLen = buf.readUnsignedByte();
					byte[] resBs = new byte[resBsLen];
					buf.readBytes(resBs);
					res[i] = resBs;
				}
				resp.setResult(res);
			}
			int errBysLen = buf.readUnsignedByte();
			if (errBysLen > 0) {
				byte[] errBys = new byte[errBysLen];
				buf.readBytes(errBys);
				resp.setError(new String(errBys));
			}
			return resp;
		}
		throw new RuntimeException("frist byte isn't 0|1(RSP|REQ)");
	}

}
