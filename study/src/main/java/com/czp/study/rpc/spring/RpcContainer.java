package com.czp.study.rpc.spring;

import org.springframework.beans.factory.InitializingBean;

import com.czp.study.rpc.net.server.RpcNettyServer;

/**
 * Function:XXX<br>
 *
 * Date :2015年12月27日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class RpcContainer implements InitializingBean {

	private RpcNettyServer ctxId;
	private String exportName;
	private Object ref;

	public RpcNettyServer getCtxId() {
		return ctxId;
	}

	public void setCtxId(RpcNettyServer ctxId) {
		this.ctxId = ctxId;
	}

	public String getExportName() {
		return exportName;
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

	public Object getRef() {
		return ref;
	}

	public void setRef(Object ref) {
		this.ref = ref;
	}

	public void afterPropertiesSet() throws Exception {
		ctxId.export(getExportName(), getRef());
	}

}
