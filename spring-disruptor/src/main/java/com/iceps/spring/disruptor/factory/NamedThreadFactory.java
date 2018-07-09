package com.iceps.spring.disruptor.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamedThreadFactory implements ThreadFactory {
	private static final Logger logger = LoggerFactory.getLogger(NamedThreadFactory.class);
	private final static Map<String, AtomicInteger> threadNumber = new ConcurrentHashMap<String, AtomicInteger>();
	private String namePrefix = "";

	public NamedThreadFactory(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	public Thread newThread(Runnable runnable) {
		AtomicInteger ai = threadNumber.get(namePrefix);
		if (ai == null) {
			synchronized (threadNumber) {
				ai = threadNumber.get(namePrefix);
				if (ai == null) {
					ai = new AtomicInteger(1);
					threadNumber.put(namePrefix, ai);
				}
			}
		}
		String n = this.namePrefix + "-" + ai.getAndIncrement();
//		logger.info("newThread {}", n);
		Thread t = new Thread(runnable, n);
		if (t.isDaemon())
			t.setDaemon(false);
		if (t.getPriority() != Thread.NORM_PRIORITY)
			t.setPriority(Thread.NORM_PRIORITY);

		return t;
	}
}