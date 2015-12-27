package com.czp.study.dubbo.main;

import java.util.Scanner;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.czp.study.dubbo.itf.IDemo;

/**
 * Function:XXX<br>
 *http://maven.outofmemory.cn/com.alibaba/dubbo/2.4.1/
 *http://www.tuicool.com/articles/QjaArm
 *
 * Date :2015年12月24日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class DubboClient {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "cli.xml" });
		context.start();
		IDemo t = (IDemo) context.getBean("czpService");
		Scanner sc = new Scanner(System.in);
		String line=null;
		while(!(line=sc.nextLine()).equals("exit")){
			System.out.println(t.hello(line));
		}
		sc.close();
		context.close();
	}
}

