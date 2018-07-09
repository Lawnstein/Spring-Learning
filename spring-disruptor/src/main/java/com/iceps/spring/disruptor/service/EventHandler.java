
package com.iceps.spring.disruptor.service;

import com.iceps.spring.disruptor.constant.EventObject;
import com.iceps.spring.disruptor.constant.EventType;

/**
 * 事件处理器.
 * 
 * @author Lawnstein.Chan
 * @version $Revision:$
 */
public interface EventHandler<T> {

	/**
	 * 获取处理的事件类型.
	 * 
	 * @return
	 */
	public EventType getEventType();

	/**
	 * 事件处理
	 * 
	 * @param eventObject
	 */
	public void handle(EventObject<T> eventObject);
	
}
