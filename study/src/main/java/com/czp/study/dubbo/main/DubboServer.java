package com.czp.study.dubbo.main;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;



/**
 * Function:XXX<br>
 *
 * Date :2015年12月24日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class DubboServer {

	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "ser.xml" });
		context.start();
		System.out.println("Press any key to exit.");
		System.in.read();
		context.close();
	}
}
