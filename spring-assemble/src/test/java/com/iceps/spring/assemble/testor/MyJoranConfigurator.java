package com.iceps.spring.assemble.testor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class MyJoranConfigurator {
	final static Logger logger = LoggerFactory.getLogger(MyJoranConfigurator.class);
	private final static String config = MyJoranConfigurator.class
			.getResource("/logback-config.xml").getFile();
	

	public static void reload() {		
		// assume SLF4J is bound to logback in the current environment
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(context);
			// Call context.reset() to clear any previous configuration, e.g. default configuration. 
			// For multi-step configuration, omit calling context.reset().
			context.reset();
//			configurator.doConfigure(args[0]);
//			configurator.doConfigure(MyJoranConfigurator.class.getResource("/com/iceps/spring/assemble/testor/logback-config.xlm").getFile());
//			URL url = MyJoranConfigurator.class.getResource("/logback-config.xlm");
			System.out.println(config);
			configurator.doConfigure(config);
		} catch (JoranException je) {
			// StatusPrinter will handle this
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(context);
		
	}
	public static void main(String[] args) {
		reload();
		
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  
		logger.info("Entering application.");
		for (int i = 0; i < 10000; i++) {
			logger.error("error Running application {}", i);
			logger.warn("warn Running application {}", i);
			logger.info("info Running application {}", i);
			logger.debug("debug Running application {}", i);
			logger.trace("trace Running application {}", i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
			}
			if (i > 0 && i % 10 == 0) {
				try {
					String r = br.readLine();
					logger.error("Readed {} : {}", i, r);	
					if (r.equals("reload"))
						reload();
				} catch (IOException e) {
				}
			}
			
		}

		logger.info("Exiting application.");
		System.exit(0);
	}
}
