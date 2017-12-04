package com.iceps.spring.redis;

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
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.iceps.spring.redis.service.RedisSequence;

@SpringBootApplication
@EnableCaching
@ImportResource({ "classpath*:applicationContext.xml" })
public class Application {
	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		applicationContext = SpringApplication.run(Application.class, args);
		testRedis();
		testSequence();
	}

	private static void testRedis() {
		final RedisTemplate<String, Object> redisTemplate = applicationContext.getBean("redisTemplate",
				RedisTemplate.class);

		// 添加一个 key
		ValueOperations<String, Object> value = redisTemplate.opsForValue();
		value.set("lp", "hello word");
		// 获取 这个 key 的值
		System.out.println(value.get("lp"));

		// 添加 一个 hash集合
		HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "lp");
		map.put("age", 26L);
		map.put("score", 98.5);
		hash.putAll("lpMap", map);
		// 获取 map
		System.out.println(hash.entries("lpMap"));

		// 添加 一个 list 列表
		ListOperations<String, Object> list = redisTemplate.opsForList();
		list.rightPush("lpList", "lp");
		list.rightPush("lpList", 26L);
		list.rightPush("lpList", 98.5);
		// 输出 list
		System.out.println(list.range("lpList", 0, 2));

		// 添加 一个 set 集合
		SetOperations<String, Object> set = redisTemplate.opsForSet();
		set.add("lpSet", "lp");
		set.add("lpSet", "26");
		set.add("lpSet", "178cm");
		// 输出 set 集合
		System.out.println(set.members("lpSet"));

		// 添加有序的 set 集合
		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
		zset.add("lpZset", "lp", 0);
		zset.add("lpZset", "26", 1);
		zset.add("lpZset", "178cm", 2);
		// 输出有序 set 集合
		System.out.println(zset.rangeByScore("lpZset", 0, 2));

	}

	private static void conccurentSequence(final int mode, final String name, final long startWith, final RedisSequence redisSequence) {

		if (mode == 0)
			redisSequence.set(name, startWith - 1);
		else
			redisSequence.reset(name, 1, startWith);
		
		int s = 10;
		final CountDownLatch countDownLatch = new CountDownLatch(s);
		List<Thread> ths = new ArrayList<Thread>();
		for (int i = 0; i < s; i++) {
			ths.add(new Thread(new Runnable() {

				@Override
				public void run() {
					for (int j = 0; j < 10; j++) {
						long s = System.currentTimeMillis();
						long v = 0;
//						if (mode == 0)
//							v = redisSequence.incrementAndGet(name, 1);
//						else
							v = redisSequence.nextval(name, 1, startWith);
						System.out.println(mode + " SEQ[" + v + "] " + (System.currentTimeMillis() - s) + "ms");
					}

					countDownLatch.countDown();
				}
			}));
		}

		for (Thread th : ths)
			th.start();

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static void testSequence() {
		final RedisTemplate redisTemplate = applicationContext.getBean("redisTemplate", RedisTemplate.class);
		RedisAtomicLong counter = new RedisAtomicLong("LAWN_ACNO_SEQ", redisTemplate.getConnectionFactory());
		counter.getAndAdd(1);
		System.out.println(counter.longValue());
		System.out.println("----------------------------------------");

		RedisSequence redisSequence = applicationContext.getBean("redisSequence", RedisSequence.class);
		conccurentSequence(0, "LAWN_ACCT_SEQ", 10, redisSequence);
		System.out.println("----------------------------------------");

		conccurentSequence(1, "LAWN_CUST_SEQ", 10, redisSequence);
		System.out.println("----------------------------------------");
	}

}
