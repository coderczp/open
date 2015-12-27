package com.czp.study.rpc.codec;

/**
 * Function:RPC回调响应<br>
 *
 * Date :2015年12月27日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class RpcResponse {

	private String error;
	private boolean isVoid;
	private byte[][] result;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public boolean isVoid() {
		return isVoid;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public byte[][] getResult() {
		return result;
	}

	public void setResult(byte[][] result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "RpcResponse [error=" + error + ", isVoid=" + isVoid + "]";
	}
}
