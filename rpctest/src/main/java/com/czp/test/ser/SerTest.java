package com.czp.test.ser;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.czp.study.rpc.net.server.RpcNettyServer;

public class SerTest {
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "cli.xml" });
		RpcNettyServer ser = (RpcNettyServer) ctx.getBean("ctx");
		ser.start();
	}
}
