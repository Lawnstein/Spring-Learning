package com.iceps.spring.assemble.service.tran;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T15002Action extends BaseAction {

	public T15002Action(String loggerName, int loopA, int loopB) {
		super(loggerName, loopA, loopB);
	}
	
	@Override
	public void pre() {
		this.setTrcode("15002");
	}
}
