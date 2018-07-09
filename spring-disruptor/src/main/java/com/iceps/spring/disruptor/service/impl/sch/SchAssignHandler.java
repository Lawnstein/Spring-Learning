package com.iceps.spring.disruptor.service.impl.sch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.iceps.spring.disruptor.constant.EventObject;
import com.iceps.spring.disruptor.constant.EventType;
import com.iceps.spring.disruptor.service.EventHandler;

@Service
public class SchAssignHandler<Map> implements EventHandler<Map> {
	private static final Logger logger = LoggerFactory.getLogger(SchAssignHandler.class);

	@Override
	public EventType getEventType() {
		return EventType.SCH_ASSIGN;
	}

	@Override
	public void handle(EventObject<Map> eventObject) {
		logger.info(Thread.currentThread().getName() + "," + this.getClass().getSimpleName() + " handle {}", eventObject);
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//		}
	}

}
