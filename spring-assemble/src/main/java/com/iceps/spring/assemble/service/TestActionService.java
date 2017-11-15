package com.iceps.spring.assemble.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iceps.spring.assemble.service.tran.BaseAction;
import com.iceps.spring.assemble.service.tran.T15001Action;
import com.iceps.spring.assemble.service.tran.T15002Action;
import com.iceps.spring.assemble.service.tran.T15003Action;

public class TestActionService {
	private final static Logger logger = LoggerFactory.getLogger(TestActionService.class);
	private CountDownLatch countDownLatch;
	private String loggerName;
	private boolean sift;

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}
	public void setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}
	public String getLoggerName() {
		return loggerName;
	}
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}
	public boolean isSift() {
		return sift;
	}
	public void setSift(boolean sift) {
		this.sift = sift;
	}
	public static Logger getLogger() {
		return logger;
	}
	public void startThreads(boolean sift) {
		this.sift = sift;
		startThreads();
	}
	public void startThreads() {
		int LOOPA = 10;
		int LOOPB = 10000;
		// int T15001N = 20;
		// int T15002N = 15;
		// int T15003N = 10;
		int T15001N = 8;
		int T15002N = 6;
		int T15003N = 10;
		logger.error("{} will start, T15001N={}, T15002N={}, T15003N={}", loggerName, T15001N, T15002N, T15003N);
		List<BaseAction> al = new ArrayList<BaseAction>();
		List<Thread> tl = new ArrayList<Thread>();
		Random r = new Random(System.currentTimeMillis());
		if (T15001N <= 0)
			T15001N = r.nextInt(20);
		for (int i = 0; i < T15001N; i++) {
			al.add(new T15001Action(loggerName, LOOPA, LOOPB));
		}
		if (T15002N <= 0)
			T15002N = r.nextInt(20);
		for (int i = 0; i < T15002N; i++) {
			al.add(new T15002Action(loggerName, LOOPA, LOOPB));
		}
		if (T15003N <= 0)
			T15003N = r.nextInt(20);
		for (int i = 0; i < T15003N; i++) {
			al.add(new T15003Action(loggerName, LOOPA, LOOPB));
		}
		countDownLatch = new CountDownLatch(al.size());
		long s = System.currentTimeMillis();
		for (BaseAction a : al) {
			if (sift)
				a.setMergmdc(true);
			a.setCountDownLatch(countDownLatch);
			a.setRandomSleep(false);
			tl.add(new Thread(a));
		}
		logger.error("{} {} thread(s) will start ...", loggerName, tl.size());
		for (Thread t : tl) {
			t.start();
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//			}
		}

		logger.error("{} =============wait for all action process over===========", loggerName);
		try {
			// 调用await方法阻塞当前线程，等待子线程完成后在继续执行
			countDownLatch.await();
		} catch (InterruptedException e) {
		}
		long e = System.currentTimeMillis();
		logger.error("{} ===============All action has completed, elapsed {}ms================", loggerName, (e - s));
	}
}
