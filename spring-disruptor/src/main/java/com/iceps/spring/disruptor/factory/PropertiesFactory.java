package com.iceps.spring.disruptor.factory;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringValueResolver;

/**
 * Spring的Properties
 * 
 * @author Lawnstein.Chan
 *
 */
@Service
public class PropertiesFactory implements EmbeddedValueResolverAware {

	private StringValueResolver stringValueResolver;

	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		stringValueResolver = resolver;
	}

	public String getStringValue(String name) {
		return stringValueResolver.resolveStringValue(name);
	}

	public int getIntValue(String name) {
		return Integer.valueOf(stringValueResolver.resolveStringValue(name));
	}

	public long getLongValue(String name) {
		return Long.valueOf(stringValueResolver.resolveStringValue(name));
	}

	public boolean getBooleanValue(String name) {		
		return Boolean.valueOf(stringValueResolver.resolveStringValue(name));
	}

}
