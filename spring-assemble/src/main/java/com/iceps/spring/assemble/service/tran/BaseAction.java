package com.iceps.spring.assemble.service.tran;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public abstract class BaseAction implements Runnable {
	private Logger logger = null;//LoggerFactory.getLogger(BaseAction.class);

	private String loggerName;
	private int loopA = 2;
	private int loopB = 10000;
	private boolean randomSleep = true;
	private boolean mergmdc = false;
	private String trcode;
	private CountDownLatch countDownLatch;

	public BaseAction(String loggerName, int loopA, int loopB) {
		this.loggerName = loggerName;
		this.logger = LoggerFactory.getLogger(this.loggerName);
		this.loopA = loopA;
		this.loopB = loopB;
	}

	public int getLoopA() {
		return loopA;
	}

	public void setLoopA(int loopA) {
		this.loopA = loopA;
	}

	public int getLoopB() {
		return loopB;
	}

	public void setLoopB(int loopB) {
		this.loopB = loopB;
	}

	public String getTrcode() {
		return trcode;
	}

	public void setTrcode(String trcode) {
		this.trcode = trcode;
	}

	public boolean isMergmdc() {
		return mergmdc;
	}

	public void setMergmdc(boolean mergmdc) {
		this.mergmdc = mergmdc;
	}

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

	public void setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}

	public boolean isRandomSleep() {
		return randomSleep;
	}

	public void setRandomSleep(boolean randomSleep) {
		this.randomSleep = randomSleep;
	}

	abstract public void pre();

	private void initMDC() {
		String[] threadAs = Thread.currentThread().getName().replaceAll("-", " ").replaceAll("_", " ").split("[ ]");
		String threadNo = String.format("%06d", (threadAs != null && threadAs.length > 0) ? Integer.valueOf(threadAs[threadAs.length - 1]) : 0);
		if (isMergmdc()) {
			MDC.put("TRCODE", trcode + "_" + threadNo);
		} else {
			MDC.put("TRCODE", trcode);
			MDC.put("THREADNO", threadNo);
		}
	}

	@Override
	public void run() {
		pre();
		initMDC();
		// String[] threadAs = Thread.currentThread().getName().replaceAll("-",
		// " ").replaceAll("_", " ").split("[ ]");
		// String threadNo = String.format("%06d", (threadAs != null &&
		// threadAs.length > 0) ? threadAs[threadAs.length - 1] : "0");
		String threadName = String.format("-%10.10s %06d", Thread.currentThread().getName(), Thread.currentThread().getId());
		logger.info("{} {} run() start >>>>", this.getClass(), threadName);
		logger.info("{} {} MDC:{}", this.getClass(), threadName, MDC.getCopyOfContextMap());
		Random r = new Random(System.currentTimeMillis());
		if (loopA < 0)
			loopA = r.nextInt(100);
		logger.info("{} {} first level, running for {} time(s)...", this.getClass(), threadName, loopA);
		for (int i = 0; i < loopA; i++) {
			logger.info("{} {} first level, loop for {}/{} time(s)...", this.getClass(), threadName, i, loopA);

			if (loopB < 0)
				loopB = r.nextInt(20000);
			logger.info("{} {} second level, running for {} time(s)...", this.getClass(), threadName, loopB);
			
			for (int j0 = 0; j0 < loopB; j0++) {
				logger.info("{} loop for ================{}/{}================= [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[ {}/{} ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]",
						threadName, i, loopA, j0, loopB);
			}


			if (randomSleep) {
				try {
					Thread.sleep(r.nextInt(10000));
				} catch (InterruptedException e) {
				}
			}

		}
		logger.info("{} {} run() over <<<<", this.getClass(), threadName);
		if (countDownLatch != null)
			countDownLatch.countDown();
	}
}
