package com.iceps.spring.assemble;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.iceps.spring.assemble.service.TestActionService;

@SpringBootApplication
@EnableCaching
@ImportResource({ "classpath*:applicationContext.xml" })
public class Application {
	private static ApplicationContext applicationContext;

	private static void testAssembleRing() {
		com.iceps.spring.assemble.service.TestActionService service = (TestActionService) applicationContext
				.getBean("assembleRing");
		service.startThreads(false);
	}

	private static void testAssembleQueue() {
		com.iceps.spring.assemble.service.TestActionService service = (TestActionService) applicationContext
				.getBean("assembleQueue");
		service.startThreads(false);
	}

	private static void testSift() {
		com.iceps.spring.assemble.service.TestActionService service = (TestActionService) applicationContext
				.getBean("sift");
		service.startThreads(true);
	}

	public static void main(String[] args) {
		applicationContext = SpringApplication.run(Application.class, args);

		for (int i = 0; i < 1000; i++) {
			testAssembleRing();
			testAssembleQueue();
			testSift();
		}
	}

}
