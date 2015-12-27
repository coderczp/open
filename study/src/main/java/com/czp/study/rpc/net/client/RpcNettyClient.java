package com.czp.study.rpc.net.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.czp.study.rpc.codec.RpcEncoder;
import com.czp.study.rpc.codec.RpcDecoder;
import com.czp.study.rpc.codec.RpcRequest;
import com.czp.study.rpc.codec.RpcResponse;

/**
 * Function:XXX<br>
 *
 * Date :2015年12月27日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class RpcNettyClient extends SimpleChannelHandler implements
		InvocationHandler {

	private int port;
	private String host;
	private Channel channel;
	private static final Object VOID_RES = new Object();
	private BlockingQueue<Object> queue = new SynchronousQueue<Object>();

	public RpcNettyClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		RpcResponse rsp = (RpcResponse) e.getMessage();
		System.out.println(rsp);
		byte[][] result = rsp.getResult();
		if (rsp.isVoid()) {
			queue.add(VOID_RES);
		} else if (result.length == 1) {
			queue.add(new String(result[0]));
		} else {
			Object[] res = new Object[result.length];
			for (int i = 0; i < res.length; i++) {
				res[i] = new String(result[i]);
			}
			queue.add(res);
		}
	}

	public Object getBean(Class<?> itf) {
		return Proxy.newProxyInstance(itf.getClassLoader(),
				new Class[] { itf }, this);
	}

	public Channel start() throws InterruptedException {
		Executor worker = Executors.newCachedThreadPool();
		Executor boss = Executors.newSingleThreadExecutor();
		ChannelFactory factory = new NioClientSocketChannelFactory(boss, worker);
		ClientBootstrap cl = new ClientBootstrap(factory);
		cl.getPipeline().addLast("decoder", new RpcDecoder());
		cl.getPipeline().addLast("dispatch", RpcNettyClient.this);
		cl.getPipeline().addLast("encoder", new RpcEncoder());
		cl.setOption("tcpNoDelay", true);
		cl.setOption("keepAlive", true);
		ChannelFuture fu = cl.connect(new InetSocketAddress(host, port));
		this.channel = fu.await().getChannel();
		return channel;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		RpcRequest req = new RpcRequest();
		req.setExportName("czpSer");
		req.setMethodName(method.getName());
		if (args != null) {
			byte[][] param = new byte[args.length][];
			for (int i = 0; i < param.length; i++) {
				param[i] = args[i].toString().getBytes();
			}
			req.setArgs(param);
		}
		channel.write(req);
		Object take = queue.take();
		return take == VOID_RES ? null : take;
	}
}
