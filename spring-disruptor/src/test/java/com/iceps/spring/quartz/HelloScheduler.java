package com.iceps.spring.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class HelloScheduler {
	public static void main(String[] args) throws SchedulerException {
		// 创建一个jobDetail的实例，将该实例与HelloJob Class绑定
		JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("myJob").build();
		// 创建一个Trigger触发器的实例，定义该job立即执行，并且每2秒执行一次，一直执行
		SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("myTrigger").startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever()).build();
		// 创建schedule实例
		StdSchedulerFactory factory = new StdSchedulerFactory();
		Scheduler scheduler = factory.getScheduler();
		scheduler.start();
		scheduler.scheduleJob(jobDetail, trigger);

	}

	public static class HelloJob implements Job {
		public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
			// 打印当前的执行时间 例如 2017-11-23 00:00:00
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("现在的时间是：" + sf.format(date));
			// 具体的业务逻辑
			System.out.println("Hello Quartz");
		}
	}
}