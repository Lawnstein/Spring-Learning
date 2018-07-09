package com.iceps.spring.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class CronScheduler {
	public static void main(String[] args) throws SchedulerException, InterruptedException {
		// jobDetail
		JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("cronJob").build();
		// cronTrigger
		// 每日的9点40触发任务
//		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("cronTrigger")
//				.withSchedule(CronScheduleBuilder.cronSchedule("0 40 9 * * ? ")).build();

		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("cronTrigger")
				.withSchedule(CronScheduleBuilder.cronSchedule("*/10 * * * * ? ")).build();
		
		// 1.每日10点15分触发 0 15 10 ？* *
		// 2.每天下午的2点到2点59分（正点开始，隔5分触发） 0 0/5 14 * * ?
		// 3.从周一到周五每天的上午10点15触发 0 15 10 ? MON-FRI
		// 4.每月的第三周的星期五上午10点15触发 0 15 10 ? * 6#3
		// 5.2016到2017年每月最后一周的星期五的10点15分触发 0 15 10 ? * 6L 2016-2017
		// Scheduler实例
		StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = stdSchedulerFactory.getScheduler();
		scheduler.start();
		scheduler.scheduleJob(jobDetail, cronTrigger);
//		scheduler.
	}

	public static class HelloJob implements Job {
		public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
			// 打印当前的执行时间 例如 2017-11-22 00:00:00
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("现在的时间是：" + sf.format(date));
			// 具体的业务逻辑
			System.out.println("开始生成任务报表 或 开始发送邮件");
		}
	}
}