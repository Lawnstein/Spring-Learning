package com.iceps.spring.disruptor.service.impl.quartz;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.iceps.spring.disruptor.constant.EventType;
import com.iceps.spring.disruptor.factory.disruptor.BatchEventFactory;
import com.iceps.spring.disruptor.service.QuartzHandler;

@Service(value = "emptyHandler")
public class EmptyHandler implements QuartzHandler {
	private static final Logger logger = LoggerFactory.getLogger(EmptyHandler.class);

	private EventType[] events = EventType.values();
	Random r = new Random(System.currentTimeMillis());

	@Override
	public void handle(Map parameter) {
		logger.info("EmptyHandler parameter : {}", parameter);

		/**
		 * fire event
		 */
		Map m = new HashMap<String, Object>() {
			{
				put("a", "b");
				put("timeStamp", System.currentTimeMillis());
				put("nanoTime", System.nanoTime());
				put("id", UUID.randomUUID().toString());
			}
		};
		m.putAll(parameter);
		BatchEventFactory.fire(events[r.nextInt(100) % events.length], m);
	}

}
