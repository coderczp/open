package com.czp.opensrource.study;

import java.util.Scanner;

import org.jboss.netty.channel.Channel;

import com.czp.study.dubbo.itf.IDemo;
import com.czp.study.rpc.codec.RpcRequest;
import com.czp.study.rpc.net.client.RpcNettyClient;

/**
 * Function:XXX<br>
 *
 * Date :2015年12月27日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class RpcCliTest {

	public static void main(String[] args) throws InterruptedException {
		RpcNettyClient c = new RpcNettyClient("127.0.0.1", 8989);
		Channel start = c.start();

		System.out.println("enter args");
		Scanner sc = new Scanner(System.in);
		IDemo demno = (IDemo) c.getBean(IDemo.class);
		
		String line;
		while (!(line = sc.nextLine()).equals("exit")) {
			System.out.println(demno.hello(line));
		}
		sc.close();
		start.close();
	}

	protected static void testByPack(Channel start, String line) {
		String[] temp = line.split(" ");
		RpcRequest req = new RpcRequest();
		req.setExportName("czpSer");
		req.setMethodName(temp[0]);
		req.setArgs(new byte[][] { temp[1].getBytes() });
		start.write(req);
	}
}
