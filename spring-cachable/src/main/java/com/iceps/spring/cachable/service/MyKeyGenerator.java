package com.iceps.spring.cachable.service;

import java.lang.reflect.Method;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

public class MyKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... params) {
		if (params == null || params.length == 0) {
			System.out.println("null params generatedKey");
			return "";
		}
		String k = "";
		for (Object p : params) {
			if (!StringUtils.isEmpty(k))
				k += ",";
			k += method.getName() + "(";
			if (p == null)
				k += "null";
			else
				k += p.getClass().getName() + ToStringBuilder.reflectionToString(p, ToStringStyle.JSON_STYLE);			
		}
		k += ")";
		System.out.println("generatedKey:" + k);
		return k;
	}

}
