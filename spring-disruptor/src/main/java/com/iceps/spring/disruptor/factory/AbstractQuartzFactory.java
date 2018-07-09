package com.iceps.spring.disruptor.factory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.iceps.spring.disruptor.service.QuartzHandler;
import com.iceps.spring.quartz.HelloScheduler.HelloJob;

/**
 * 定时任务工厂.
 * 
 * @author Lawnstein.Chan
 * @version $Revision:$
 */
public class AbstractQuartzFactory implements ApplicationContextAware, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(AbstractQuartzFactory.class);

	protected final static StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();

	protected ApplicationContext applicationContext;

	public final static String KEY_GROUP_NAME = "groupName";

	public final static String KEY_JOB_NAME = "jobName";

	/**
	 * 配置项:组名
	 */
	protected String groupName = "DEFUALT";

	/**
	 * 配置项:组名
	 */
	protected final static String DEFAULT_JOB_NAME = "*";

	/**
	 * 配置项:任务Cron表达式映射
	 */
	protected Map<String, String> cronExpressions;

	/**
	 * 配置项:任务处理器映射
	 */
	protected Map<String, QuartzHandler> handlers;

	/**
	 * 运行项:全局处理器映射.
	 */
	protected static Map<String, QuartzHandler> globalHandlers = new ConcurrentHashMap<String, QuartzHandler>();

	/**
	 * 配置项:任务指定参数
	 */
	protected Map<String, String> parameters;

	/**
	 * 运行项:调度容器
	 */
	protected Scheduler scheduler;

	/**
	 * 运行项:任务Cron表达式映射
	 */
	protected Map<String, String> jobCronExpressions;

	/**
	 * 运行项:任务参数映射
	 */
	protected Map<String, String> jobParameters;

	/**
	 * 运行项:任务
	 */
	protected Map<String, JobDetail> jobDetails;

	/**
	 * 运行项:任务触发器键
	 */
	protected Map<String, TriggerKey> jobTriggerKeys;

	/**
	 * 运行项:任务触发器
	 */
	protected Map<String, CronTrigger> jobTriggers;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Map<String, String> getCronExpressions() {
		return cronExpressions;
	}

	public void setCronExpressions(Map<String, String> cronExpressions) {
		this.cronExpressions = cronExpressions;
	}

	public void addCronExpression(String jobName, String cronExpression) {
		if (this.cronExpressions == null) {
			this.cronExpressions = new HashMap<String, String>();
		}
		this.cronExpressions.put(jobName, cronExpression);
	}

	public void removeCronExpression(String jobName) {
		if (this.cronExpressions == null) {
			return;
		}
		this.cronExpressions.remove(jobName);
	}

	public Map<String, QuartzHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(Map<String, QuartzHandler> handlers) {
		this.handlers = handlers;
		globalHandlers.putAll(handlers);
	}

	public void addHandlers(String jobName, QuartzHandler handler) {
		if (this.handlers == null) {
			this.handlers = new HashMap<String, QuartzHandler>();
		}
		this.handlers.put(jobName, handler);
		globalHandlers.put(jobName, handler);
	}

	public void removeHandlers(String jobName) {
		if (this.handlers == null) {
			return;
		}
		this.handlers.remove(jobName);
		globalHandlers.remove(jobName);
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public void start() {
		if (scheduler == null) {
			try {
				scheduler = schedulerFactory.getScheduler(groupName);
			} catch (SchedulerException e) {
				logger.error("get Scheduler for {} SchedulerException.", groupName, e);
				throw new RuntimeException("Get scheduler failed, " + e.getMessage(), e);
			}
		}
		initConfig();
		int err = 0;
		String jobName = null;
		String jobCron = null;
		for (Entry<String, String> e : cronExpressions.entrySet()) {
			jobName = e.getKey();
			jobCron = e.getValue();

			boolean b = registerJob(jobName, jobCron, (String) getOrDefault(parameters, jobName, DEFAULT_JOB_NAME));
			if (!b) {
				err++;
				continue;
			}

		}
		if (err > 0) {
			logger.error("Start Scheduler {} got {} error(s).", groupName, err);
			throw new RuntimeException("Start Scheduler " + groupName + " got " + err + " error(s).");
		}
	}

	public void stop() {
		try {
			if (!scheduler.isShutdown()) {
				scheduler.shutdown();
			}
		} catch (SchedulerException e) {
			logger.error("Stop Scheduler {} SchedulerException.", groupName, e);
		}
	}

	private void initConfig() {
		if (cronExpressions == null) {
			cronExpressions = new HashMap<String, String>();
		}
		if (handlers == null) {
			handlers = new HashMap<String, QuartzHandler>();
		}
		if (parameters == null) {
			parameters = new HashMap<String, String>();
		}
		if (jobCronExpressions == null) {
			jobCronExpressions = new HashMap<String, String>();
		}
		if (jobParameters == null) {
			jobParameters = new HashMap<String, String>();
		}
		if (jobDetails == null) {
			jobDetails = new HashMap<String, JobDetail>();
		}
		if (jobTriggerKeys == null) {
			jobTriggerKeys = new HashMap<String, TriggerKey>();
		}
		if (jobTriggers == null) {
			jobTriggers = new HashMap<String, CronTrigger>();
		}
	}

	private Object getOrDefault(Map map, Object key, Object defaultKey) {
		Object o = map.get(key);
		if (o == null) {
			o = map.get(defaultKey);
		}
		return o;
	}

	private boolean registerJob(String jobName, String jobCronExpression, String jobParameter) {
		if (jobDetails.get(jobName) == null) {

			QuartzHandler handler = (QuartzHandler) getOrDefault(handlers, jobName, DEFAULT_JOB_NAME);
			if (handler == null) {
				logger.error("Invalid job {} config, no handler matched.", jobName);
				return false;
			}
			if (jobCronExpression == null) {
				jobCronExpression = "";
			}
			if (jobParameter == null) {
				jobParameter = "";
			}

			jobCronExpressions.put(jobName, jobCronExpression);
			jobParameters.put(jobName, jobParameter);

			JobDataMap dataMap = new JobDataMap(getPropertyKeyValues(jobParameter));
			dataMap.put(KEY_GROUP_NAME, groupName);
			dataMap.put(KEY_JOB_NAME, jobName);
			JobDetail jobDetail = JobBuilder.newJob(InnerJob.class).withIdentity(jobName).usingJobData(dataMap)
					.storeDurably(true).build();
			jobDetails.put(jobName, jobDetail);
			CronTrigger jobTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName)
					.withSchedule(CronScheduleBuilder.cronSchedule(jobCronExpression)).build();
			jobTriggers.put(jobName, jobTrigger);
			jobTriggerKeys.put(jobName, TriggerKey.triggerKey(jobName, groupName));
			try {
				Date triggerDate = scheduler.scheduleJob(jobDetail, jobTrigger);
				logger.info("scheduleJob {} over, triggerDate {}", jobName, triggerDate);
			} catch (SchedulerException e1) {
				logger.error("scheduleJob {} SchedulerException.", jobName, e1);
				return false;
			}

		} else {
			String orgJobParameter = jobParameters.get(jobName);
			String orgJobCronExpression = jobCronExpressions.get(jobName);
			if (orgJobParameter.equals(jobParameter) && orgJobCronExpression.equals(jobCronExpression)) {
				return true;
			}

			TriggerKey triggerKey = jobTriggerKeys.get(jobName);

			if (!orgJobParameter.equals(jobParameter)) {
				logger.info("updateJobParameter {} from {}  to {}", jobName, orgJobParameter, jobParameter);
				JobDataMap dataMap = new JobDataMap(getPropertyKeyValues(jobParameter));
				logger.info("updateJobDataMap {} from {}  to {}", jobName, jobDetails.get(jobName).getJobDataMap(),
						dataMap);
				try {
					scheduler.pauseTrigger(triggerKey);
					jobDetails.get(jobName).getJobDataMap().clear();
					jobDetails.get(jobName).getJobDataMap().putAll(dataMap);
					scheduler.resumeTrigger(triggerKey);
				} catch (SchedulerException e) {
					logger.error("pauseTrigger {} SchedulerException.", jobName, e);
					throw new RuntimeException("pauseTrigger " + jobName + " SchedulerException, " + e.getMessage());
				}
			}

			if (!orgJobCronExpression.equals(jobCronExpression)) {
				logger.info("updateJobCronExpression {} from {}  to {}", jobName, orgJobCronExpression,
						jobCronExpression);
				CronTrigger jobTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName)
						.withSchedule(CronScheduleBuilder.cronSchedule(jobCronExpression)).build();
				jobTriggers.put(jobName, jobTrigger);
				try {
					scheduler.rescheduleJob(triggerKey, jobTrigger);
				} catch (SchedulerException e) {
					logger.error("rescheduleJob {} SchedulerException.", jobName, e);
					throw new RuntimeException("rescheduleJob " + jobName + " SchedulerException, " + e.getMessage());
				}
			}

			jobParameters.put(jobName, jobParameter);
			jobCronExpressions.put(jobName, jobCronExpression);
		}

		return true;
	}

	private boolean removeJob(String jobName) {
		try {
			TriggerKey triggerKey = jobTriggerKeys.get(jobName);
			scheduler.pauseTrigger(triggerKey);
			scheduler.unscheduleJob(triggerKey);
			scheduler.deleteJob(JobKey.jobKey(jobName, groupName));
		} catch (SchedulerException e) {
			logger.error("unscheduleJob {} SchedulerException.", jobName, e);
			return false;
		}
		return true;
	}

	protected static Map getPropertyKeyValues(String keyValuePairs) {
		Map<String, String> map = new HashMap<String, String>();
		if (keyValuePairs == null || keyValuePairs.length() == 0) {
			return map;
		}
		String[] p1 = keyValuePairs.split("[;,]");
		for (String p2 : p1) {
			if (p2 == null) {
				continue;
			}
			p2 = p2.trim();
			if (p2 == null || p2.length() == 0) {
				continue;
			}
			String[] p3 = getPropertyKeyValue(p2);
			if (p3 != null) {
				String n = p3[0].trim();
				String v = p3[1].trim();
				map.put(n, v);
				logger.info("jobParameter " + n + "=[" + v + "]");
			} else {
				logger.error("不合法的参数格式:" + keyValuePairs + " (" + p2 + ")");
			}
		}
		return map;
	}

	protected static String[] getPropertyKeyValue(String keyValuePair) {
		int p1 = keyValuePair.indexOf("=");
		int p2 = keyValuePair.indexOf(":");
		int p = p1 > p2 ? p1 : p2;
		if (p > 0)
			return new String[] { keyValuePair.substring(0, p), keyValuePair.substring(p + 1) };
		return null;
	}

	static class InnerJob implements org.quartz.Job {
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			System.out.println("JobDataMap:" + dataMap);
			globalHandlers.get(context.getJobDetail().getKey().getName()).handle(dataMap);
		}
	}
}
