package com.iceps.spring.disruptor.factory.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 动态Quartz触发工厂: 动态从数据库配置加载.
 * 
 * @author Lawnstein.Chan
 *
 */
public class DynamicQuartzFactory extends AbstractQuartzFactory {
	private static final Logger logger = LoggerFactory.getLogger(DynamicQuartzFactory.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		initConfig();
		
		//TODO load cron & parameter from database.
		//TODO scan change on cron & parameter, and reload it.
	}

}
