package com.czp.study.rpc.codec;

/**
 * Function:RPC请求对象<br>
 *
 * Date :2015年12月27日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class RpcRequest {

	private String exportName;
	private String methodName;
	private byte[][] args;

	public String getExportName() {
		return exportName;
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public byte[][] getArgs() {
		return args;
	}

	public void setArgs(byte[][] args) {
		this.args = args;
	}
}
