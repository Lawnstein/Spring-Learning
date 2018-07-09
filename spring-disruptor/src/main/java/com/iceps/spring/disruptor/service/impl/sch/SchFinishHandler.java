package com.iceps.spring.disruptor.service.impl.sch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.iceps.spring.disruptor.constant.EventObject;
import com.iceps.spring.disruptor.constant.EventType;
import com.iceps.spring.disruptor.service.EventHandler;

@Service
public class SchFinishHandler<Map> implements EventHandler<Map> {
	private static final Logger logger = LoggerFactory.getLogger(SchFinishHandler.class);

	@Override
	public EventType getEventType() {
		return EventType.SCH_FINISH;
	}

	@Override
	public void handle(EventObject<Map> eventObject) {
		logger.info(Thread.currentThread().getName() + "," + this.getClass().getSimpleName() + " handle {}", eventObject);
	}

}
