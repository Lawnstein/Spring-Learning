package com.iceps.spring.rocketmq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.iceps.spring.redis.service.RmqAsyncCaller;

@SpringBootApplication
@EnableCaching
@ImportResource({ "classpath*:applicationContext.xml" })
public class Application {
	private static ApplicationContext applicationContext;
	private static RmqAsyncCaller caller;
	private static String trnCd = "lawnstein";

	public static void main(String[] args) {
		applicationContext = SpringApplication.run(Application.class, args);
		caller = applicationContext.getBean("caller", RmqAsyncCaller.class);
		startConsumer();
		startProducer();
	}

	private static void startConsumer() {
		caller.serverStart(trnCd, 4, null);
	}

	private static void startProducer() {
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			Map m = new HashMap();
			m.put("TrnCd", trnCd);
			m.put("seq-" + i, System.currentTimeMillis());
			caller.clientCall(m);
		}
	}


}
