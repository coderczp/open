package com.czp.study.rpc.spring;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * Function:自定义spring标签解析器<br>
 * http://docs.spring.io/spring/docs/3.1.x/spring-framework-reference/html/
 * extensible-xml.html
 *
 * Date :2015年12月25日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class RpcBeanDefParser extends AbstractSingleBeanDefinitionParser {

	protected Class<RpcContainer> getBeanClass(Element element) {
		return RpcContainer.class;
	}

	protected void doParse(Element elt, BeanDefinitionBuilder bean) {
		String exportName = elt.getAttribute("exportName");
		String ctxId = elt.getAttribute("ctxId");
		String refId = elt.getAttribute("ref");
		
		bean.addPropertyReference("ref", refId);
		bean.addPropertyReference("ctxId", ctxId);
		bean.addPropertyValue("exportName", exportName);
	}

}
