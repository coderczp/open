package com.czp.study.rpc.net.server;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.czp.study.rpc.codec.RpcEncoder;
import com.czp.study.rpc.codec.RpcDecoder;
import com.czp.study.rpc.codec.RpcRequest;
import com.czp.study.rpc.codec.RpcResponse;

/**
 * Function:接收前端请求,分发调用<br>
 *
 * Date :2015年12月27日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class RpcNettyServer extends SimpleChannelHandler {

	private int port;
	private Channel server;
	private ExecutorService worker = Executors.newCachedThreadPool();
	private ExecutorService boss = Executors.newSingleThreadExecutor();
	private Map<String, Object> rpcServers = new HashMap<String, Object>();
	private Map<String, Map<String, Method>> rpcMethods = new HashMap<String, Map<String, Method>>();

	public RpcNettyServer(int port) {
		this.port = port;
	}

	/**
	 * 导出RPC服务
	 * 
	 * @param exportName
	 * @param implServer
	 */
	public void export(String exportName, Object implServer) {
		if (rpcServers.containsKey(exportName))
			return;

		Map<String, Method> map = new HashMap<String, Method>();
		for (Method method : implServer.getClass().getDeclaredMethods()) {
			map.put(method.getName(), method);
		}
		rpcServers.put(exportName, implServer);
		rpcMethods.put(exportName, map);
		System.out.println("success export:"+exportName);
	}

	public void start() {

		ChannelFactory factory = new NioServerSocketChannelFactory(boss, worker);
		ServerBootstrap bt = new ServerBootstrap(factory);
		ChannelPipeline pipeline = bt.getPipeline();
		pipeline.addLast("decoder", new RpcDecoder());
		pipeline.addLast("dispatch", RpcNettyServer.this);
		pipeline.addLast("encoder", new RpcEncoder());
		System.out.println("server is runing at:" + port);
		server = bt.bind(new InetSocketAddress(port));
	}

	/**
	 * 关闭socket服务
	 */
	public void shutdown() {
		server.close();
		boss.shutdown();
		worker.shutdown();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Object message = e.getMessage();
		if (!(message instanceof RpcRequest))
			throw new RuntimeException("unsupport type:" + message);

		Channel channel = ctx.getChannel();
		RpcResponse rsp = new RpcResponse();
		RpcRequest req = (RpcRequest) message;
		String exportName = req.getExportName();
		Object ser = rpcServers.get(exportName);
		if (ser == null) {
			rsp.setError(exportName + " isn't found");
			channel.write(rsp);
			return;
		}

		Map<String, Method> map = rpcMethods.get(exportName);
		String methodName = req.getMethodName();
		Method method = map.get(methodName);
		// method not found
		if (method == null) {
			rsp.setError(exportName + "." + methodName + " isn't found");
			channel.write(rsp);
			return;
		}
		byte[][] args = req.getArgs();
		Object[] param = new String[args.length];
		for (int i = 0; i < param.length; i++) {
			param[i] = new String(args[i]);
		}
		try {
			Object invoke = method.invoke(ser, param);
			byte[][] result = { invoke.toString().getBytes() };
			rsp.setResult(result);
		} catch (Exception e1) {
			rsp.setError(e1.toString());
			e1.printStackTrace();
		}
		rsp.setVoid(false);
		channel.write(rsp);
		return;
	}

}
