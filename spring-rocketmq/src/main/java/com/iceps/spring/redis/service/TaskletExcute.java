package com.iceps.spring.redis.service;

/**
 * 任务处理接口.
 * @author Lawnstein.Chan 
 * @version $Revision:$
 */
public interface TaskletExcute<I, O> {

	/**
	 * 数据处理:针对每组数据的业务处理.
	 * 
	 * @return
	 */
	public O excute(I itemData);
}
