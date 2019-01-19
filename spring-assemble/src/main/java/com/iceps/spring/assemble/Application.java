package com.iceps.spring.assemble;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.iceps.spring.assemble.service.TestActionService;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

@SpringBootApplication
@EnableCaching
@ImportResource({ "classpath*:applicationContext.xml" })
public class Application {
	private static ConfigurableApplicationContext applicationContext;

	private static void testAssembleSync() {
		com.iceps.spring.assemble.service.TestActionService service = (TestActionService) applicationContext
				.getBean("assembleSync");
		service.startThreads(false);
	}

	private static void testAssembleAsyn() {
		com.iceps.spring.assemble.service.TestActionService service = (TestActionService) applicationContext
				.getBean("assembleAsyn");
		service.startThreads(false);
	}
	
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

	public static void reload(String externalConfigFileLocation) throws IOException, JoranException {
		System.out.println("-=-=-= try Reset Logback loggerContext with " + externalConfigFileLocation + " =-=-=- ");

		InputStream is = Application.class.getResourceAsStream(externalConfigFileLocation);
		if (is == null) {
			throw new IOException("Logback External Config File Parameter does not reference a file that exists");
		}
//		File externalConfigFile = new File(externalConfigFileLocation);
//		if (!externalConfigFile.exists()) {
//			throw new IOException("Logback External Config File Parameter does not reference a file that exists");
//		}
//
//		if (!externalConfigFile.isFile()) {
//			throw new IOException("Logback External Config File Parameter exists, but does not reference a file");
//		}
//
//		if (!externalConfigFile.canRead()) {
//			throw new IOException("Logback External Config File exists and is a file, but cannot be read.");
//		}

		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(lc);
		lc.getStatusManager().clear();
		lc.reset();
		configurator.doConfigure(is);
		StatusPrinter.printInCaseOfErrorsOrWarnings(lc);

		System.out.println("-=-=-= Reset Logback loggerContext with " + externalConfigFileLocation + " Over =-=-=- ");
	}

	private static void loggerContextReset() {

		System.out.println("-=-=-= try Reset Logback loggerContext =-=-=- ");
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		// 必须清空一下，否则之前加载的logger堆栈信息还保留着StatusPrinter.print会打印出之前的状态
		loggerContext.getStatusManager().clear();
		loggerContext.reset();

		System.out.println("-=-=-=  Logback loggerContext , try to reload config =-=-=- ");
		ContextInitializer ci = new ContextInitializer(loggerContext);
		try {
			ci.autoConfig();
			System.out.println("-=-=-=  Logback loggerContext reload over =-=-=- ");
		} catch (JoranException e) {
			System.err.println("-=-=-= Reset Logback status Failed =-=-=- \n" + e);
		}
	}

	private static void startLoggerResetThreads() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				
				for (int i = 0; i < 3; i++) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
					try {
						// loggerContextReset();
						reload("/logback.xml.reload");
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JoranException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();

	}

	private static void stop() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("222222222222222222222222222222222>>>>>>>>>>>>>>>");
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
				}

				System.out.println("333333333333333333333333333333333333333333333333333>>>>>>>>>>>>>>>");
				applicationContext.close();
				// System.exit(0);
				System.out.println("4444444444444444444444444444444444444444444444444444<<<<<<<<<<<<<<<");
			}
		}).start();
	}

	public static void main(String[] args) {
		System.out.println("0000000000000000000000000000000000>>>>>>>>>>>>>>>");
		applicationContext = SpringApplication.run(Application.class, args);
		System.out.println("1111111111111111111111111111111111>>>>>>>>>>>>>>>");
//		stop();
//		startLoggerResetThreads();
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA>>>>>>>>>>>>>>>");
		int l = 1;
		for (int i = 0; i < l; i++) {
			testAssembleSync();
//			testAssembleAsync();
			
//			testAssembleRing();
//			testAssembleQueue();
//			testSift();
		}
	}

}
