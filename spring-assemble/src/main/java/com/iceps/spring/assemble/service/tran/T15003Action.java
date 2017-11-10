package com.iceps.spring.assemble.service.tran;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class T15003Action extends BaseAction {

	public T15003Action(String loggerName, int loopA, int loopB) {
		super(loggerName, loopA, loopB);
	}

	@Override
	public void pre() {
		this.setTrcode("15003");
	}
	
}
