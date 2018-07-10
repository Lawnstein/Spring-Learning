package com.iceps.spring.disruptor.factory.quartz;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Properties;

import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StdSchedulerFactory extends org.quartz.impl.StdSchedulerFactory {
	private static final Logger logger = LoggerFactory.getLogger(StdSchedulerFactory.class);

	private String instanceName;

	public StdSchedulerFactory(String instanceName) throws SchedulerException {
		this.instanceName = instanceName;
	}

	public void initialize() throws SchedulerException {

		String requestedFile = System.getProperty(PROPERTIES_FILE);
		String propFileName = requestedFile != null ? requestedFile : "quartz.properties";
		File propFile = new File(propFileName);
		Properties props = new Properties();
		InputStream in = null;
		String propSrc = null;
		try {
			if (propFile.exists()) {
				try {
					if (requestedFile != null) {
						propSrc = "specified file: '" + requestedFile + "'";
					} else {
						propSrc = "default file in current working dir: 'quartz.properties'";
					}
					logger.debug(propSrc);

					in = new BufferedInputStream(new FileInputStream(propFileName));
					props.load(in);

				} catch (IOException ioe) {
					throw new SchedulerException("Properties file: '" + propFileName + "' could not be read.", ioe);
				}
			} else if (requestedFile != null) {
				in = Thread.currentThread().getContextClassLoader().getResourceAsStream(requestedFile);
				if (in == null) {
					throw new SchedulerException("Properties file: '" + requestedFile + "' could not be found.");
				}

				propSrc = "specified file: '" + requestedFile + "' in the class resource path.";
				logger.debug(propSrc);
				in = new BufferedInputStream(in);
				try {
					props.load(in);
				} catch (IOException ioe) {
					throw new SchedulerException("Properties file: '" + requestedFile + "' could not be read.", ioe);
				}
			} else {
				propSrc = "default resource file in Quartz package: 'quartz.properties'";
				logger.debug(propSrc);
				
				ClassLoader cl = getClass().getClassLoader();
				if (cl == null)
					cl = findClassloader();
				if (cl == null)
					throw new SchedulerConfigException("Unable to find a class loader on the current thread or class.");

				in = cl.getResourceAsStream("quartz.properties");
				if (in == null) {
					in = cl.getResourceAsStream("/quartz.properties");
				}
				if (in == null) {
					in = cl.getResourceAsStream("org/quartz/quartz.properties");
				}
				if (in == null) {
					throw new SchedulerException("Default quartz.properties not found in class path");
				}
				try {
					props.load(in);
				} catch (IOException ioe) {
					throw new SchedulerException("Resource properties file: 'org/quartz/quartz.properties' "
							+ "could not be read from the classpath.", ioe);
				}
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ignore) {
					/* ignore */ }
			}
		}

		Properties sysProps = null;
		try {
			sysProps = System.getProperties();
		} catch (AccessControlException e) {
			logger.warn("Skipping overriding quartz properties with System properties "
					+ "during initialization because of an AccessControlException.  "
					+ "This is likely due to not having read/write access for "
					+ "java.util.PropertyPermission as required by java.lang.System.getProperties().  "
					+ "To resolve this warning, either add this permission to your policy file or "
					+ "use a non-default version of initialize().", e);
		}
		if (sysProps != null) {
			props.putAll(sysProps);
		}

		props.put(PROP_SCHED_INSTANCE_NAME, this.instanceName);
		initialize(props);
	}

	private ClassLoader findClassloader() {
		// work-around set context loader for windows-service started jvms (QUARTZ-748)
		if (Thread.currentThread().getContextClassLoader() == null && getClass().getClassLoader() != null) {
			Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
		}
		return Thread.currentThread().getContextClassLoader();
	}

}
