package com.iceps.spring.cachable;

import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.iceps.spring.cachable.service.User;
import com.iceps.spring.cachable.service.UserService;

@SpringBootApplication
@EnableCaching
@ImportResource({ "classpath*:applicationContext.xml" })
public class Application {
	private static ApplicationContext applicationContext;

	private static void testCache1() {
		UserService service = applicationContext.getBean(UserService.class);
		Random r = new Random(System.currentTimeMillis());
		for (int i = 0; i < 100; i++) {
			User u = service.getUser((long) 10);
			System.out.println(u);
			try {
				long s = r.nextInt(4);
				System.out.println("sleep for " + s + " second(s).");
				Thread.sleep(1000 * s);
			} catch (InterruptedException e) {
			}
		}
	}

	private static void testCache2() {
		UserService service = applicationContext.getBean(UserService.class);
		Random r = new Random(System.currentTimeMillis());
		User u = new User();
		u.setId((long) 10);
		u.setName("CACHE");
		for (int i = 0; i < 100; i++) {
			u = new User();
			u.setId((long) 10);
			u.setName("CACHE");
			User user = service.getByUserEntity(u);
			System.out.println(u);
			try {
				long s = r.nextInt(4);
				System.out.println("sleep for " + s + " second(s).");
				Thread.sleep(1000 * s);
			} catch (InterruptedException e) {
			}
		}
	}

	private static void testPool1() {
		final ThreadPoolTaskExecutor pool = applicationContext.getBean(ThreadPoolTaskExecutor.class);
		int size = pool.getMaxPoolSize();
		for (int i = 0; i < 10; i++) {
			if (pool.getMaxPoolSize() < i + 1) {
				pool.setMaxPoolSize(i + 1);
			}
			pool.execute(new Runnable() {

				@Override
				public void run() {

					for (int j = 0; j < 10000;) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
						}
						j += 10;
						
//						if (j % 100 == 0)
//							System.out.println(Thread.currentThread() + " " + j);
						
					}
					
					System.out.println(Thread.currentThread() + " run over, pool.activeCount=" + pool.getActiveCount() + ", pool.corePoolSize=" + pool.getCorePoolSize() + ", pool.maxPoolSize=" + pool.getMaxPoolSize());
				}
			});
			
			System.out.println(Thread.currentThread() + " ThreadPoolTaskExecutor-" + i + " started, pool.activeCount=" + pool.getActiveCount() + ", pool.corePoolSize=" + pool.getCorePoolSize() + ", pool.maxPoolSize=" + pool.getMaxPoolSize());
//			pool.setMaxPoolSize(size);
		}
	}

	private static void testPool2() {
		ThreadPoolTaskExecutor pool = applicationContext.getBean(ThreadPoolTaskExecutor.class);
		int size = pool.getMaxPoolSize();
		for (int i = 0; i < 10; i++) {
			if (pool.getMaxPoolSize() < i + 1) {
				pool.setMaxPoolSize(i + 1);
			}
			pool.execute(new Runnable() {

				@Override
				public void run() {

					for (int j = 0; j < 100000;) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
						}
						j += 10;
						if (j % 100 == 0)
							System.out.println(Thread.currentThread() + " " + j);
					}
				}
			});
			System.out.println("ThreadPoolTaskExecutor-" + i + " started.");
			pool.setMaxPoolSize(size);
		}
	}

	public static void main(String[] args) {
		applicationContext = SpringApplication.run(Application.class, args);
		// testCache2();
		new Thread(new Runnable() {

			@Override
			public void run() {
				testPool1();
			}
		}).start();
		testPool1();
	}

}
