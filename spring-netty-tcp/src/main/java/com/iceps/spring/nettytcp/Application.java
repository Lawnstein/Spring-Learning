package com.iceps.spring.nettytcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableCaching
@ImportResource({ "classpath*:applicationContext.xml" })
public class Application {
	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		applicationContext = SpringApplication.run(Application.class, args);
		System.out.println(Application.class.getName() + " started ...");
		runAndExit(20000);
	}

	public static void runAndExit(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
		System.out.println(Application.class.getName() + " exit ...");
		System.exit(0);
	}
}
