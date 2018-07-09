package com.iceps.spring.quartz;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 功能:定时群发工作任务
 * 
 * @author dengyu
 * @date 2017-07-22
 */
public class TimedMassJob implements org.quartz.Job {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		System.out.println("JobDataMap:" + dataMap);
	}
}
