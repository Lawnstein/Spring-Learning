package com.iceps.spring.disruptor.constant;

/**
 * 事件对象.
 * 
 * @author Lawnstein.Chan
 * @version $Revision:$
 */
public class EventObject<T> {

	private static final long serialVersionUID = -5931486734269692733L;

	protected EventType eventType;

	protected T eventArgs;

	public EventObject() {
	}

	public EventObject(EventType eventType, T eventArgs) {
		this.eventArgs = eventArgs;
		this.eventType = eventType;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public T getEventArgs() {
		return eventArgs;
	}

	public void setEventArgs(T eventArgs) {
		this.eventArgs = eventArgs;
	}

	@Override
	public String toString() {
		return "EventObject [eventType=" + eventType + ", eventArgs=" + eventArgs + "]";
	}

}
