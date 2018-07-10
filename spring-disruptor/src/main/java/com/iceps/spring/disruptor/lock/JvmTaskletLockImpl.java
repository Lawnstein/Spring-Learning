package com.iceps.spring.disruptor.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JvmTaskletLockImpl implements TaskletLock {
	private static final Logger logger = LoggerFactory.getLogger(JvmTaskletLockImpl.class);

	@Override
	public boolean lock(String key, Runnable tasklet) {
		logger.info("lock {} started.", key);
		tasklet.run();
		logger.info("lock {} closed.", key);
		return true;
	}

	@Override
	public boolean lock(String key, long timeoutSec, Runnable tasklet) {
		logger.info("lock {} started.", key);
		tasklet.run();
		logger.info("lock {} closed.", key);
		return true;
	}

	@Override
	public boolean lock(String key, long lockSec) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean lock(String key, long timeoutSec, long lockSec) {
		// TODO Auto-generated method stub
		return false;
	}

}
