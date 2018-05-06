package com.iceps.spring.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskletExcutor<Map, Object> implements TaskletExcute<Map, Object> {
	protected final static Logger logger = LoggerFactory.getLogger(TaskletExcutor.class);

	@Override
	public Object excute(Map itemData) {
		logger.info("TaskletExcutor >>>> itemData : {}", itemData );		
		return null;
	}

}
