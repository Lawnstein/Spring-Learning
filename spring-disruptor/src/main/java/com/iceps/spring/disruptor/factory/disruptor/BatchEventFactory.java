package com.iceps.spring.disruptor.factory.disruptor;

import org.springframework.util.Assert;

import com.iceps.spring.disruptor.constant.EventType;

/**
 * 批量事件调度工厂.
 * 
 * @author Lawnstein.Chan
 * @version $Revision:$
 */
public class BatchEventFactory extends AbstractEventFactory {

	private static AbstractEventFactory instance = null;

	public BatchEventFactory() {
		super();
		instance = this;
	}

	/**
	 * 触发事件
	 * @param eventType 事件类型
	 * @param eventArgs 附加参数
	 */
	public static void fire(EventType eventType, Object eventArgs) {
		Assert.notNull(eventType, "eventType cannot be null");
		Assert.notNull(instance, "BatchEventFactory instance not initialized.");		
		instance.fireEvent(eventType, eventArgs);
	}
}
