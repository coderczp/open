package com.czp.study.dubbo.impl;

import com.czp.study.dubbo.itf.IDemo;

/**
 * Function:XXX<br>
 *
 * Date :2015年12月24日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class MyDemoServer implements IDemo {

	public String hello(String line) {
		double random = Math.random();
		if(random>0.5){
			System.out.println(random);
			throw new RuntimeException("e");
		}
		return line;
	}

}

