package com.czp.opensrource.study;

import com.czp.study.dubbo.impl.MyDemoServer;
import com.czp.study.dubbo.itf.IDemo;
import com.czp.study.rpc.net.server.RpcNettyServer;

/**
 * Function:XXX<br>
 *
 * Date :2015年12月25日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class CzpSpringMain {

	public static void main(String[] args) {
		RpcNettyServer s = new RpcNettyServer(8990);
		IDemo demo = new MyDemoServer();
		s.export("czpSer",demo);
		s.start();
		
	}
}
