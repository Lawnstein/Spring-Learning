
package com.iceps.spring.disruptor.service;

import java.util.Map;

/**
 * 定时任务处理器.
 * 
 * @author Lawnstein.Chan
 * @version $Revision:$
 */
public interface QuartzHandler {

	/**
	 * 处理
	 * 
	 * @param parameter
	 */
	public void handle(Map parameter);

}
