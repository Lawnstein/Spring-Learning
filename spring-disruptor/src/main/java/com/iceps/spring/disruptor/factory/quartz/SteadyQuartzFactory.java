package com.iceps.spring.disruptor.factory.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 静态Quartz触发工厂: 采用XML配置加载生效.
 * 
 * @author Lawnstein.Chan
 *
 */
public class SteadyQuartzFactory extends AbstractQuartzFactory {
	private static final Logger logger = LoggerFactory.getLogger(SteadyQuartzFactory.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		initConfig();
	}

}
