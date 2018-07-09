package com.iceps.spring.disruptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.iceps.spring.disruptor.constant.EventType;
import com.iceps.spring.disruptor.factory.BatchEventFactory;

@SpringBootApplication
@EnableCaching
@ImportResource({ "classpath*:applicationContext.xml" })
public class Application {
	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		applicationContext = SpringApplication.run(Application.class, args);
		fireEvents();
	}

	public static void fireEvents() {
//		com.iceps.spring.disruptor.factory.AbstractEventFactory ef = (com.iceps.spring.disruptor.factory.AbstractEventFactory) applicationContext
//				.getBean(com.iceps.spring.disruptor.factory.AbstractEventFactory.class);

		EventType[] events = EventType.values();
		Random r = new Random(System.currentTimeMillis());
		for (int k = 0; k < 100; k++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 1000; i++) {
						/**
						 * fire event
						 */
						BatchEventFactory.fire(events[r.nextInt(100) % events.length], new HashMap<String, Object>() {
							{
								put("a", "b");
								put("timeStamp", System.currentTimeMillis());
								put("nanoTime", System.nanoTime());
								put("id", UUID.randomUUID().toString());
							}
						});
					}
				}
			}).start();
		}
	}

}
