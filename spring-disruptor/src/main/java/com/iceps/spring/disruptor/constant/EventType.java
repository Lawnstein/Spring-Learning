
package com.iceps.spring.disruptor.constant;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 调度事件.
 * 
 * @author Lawnstein.Chan
 * @version $Revision:$
 */
public enum EventType {
	/**
	 * 主任务调度类.
	 */
	SCH_ASSIGN("sch", "作业分配"),
	SCH_CALL("sch", "作业调度"),
	SCH_FEEDBACK("sch", "作业汇报"),
	SCH_QUERY("sch", "作业查询"),
	SCH_FINISH("sch", "任务结束"),

	/**
	 * 异常保全类.
	 */
	SCAN_JOB_UNDONE("scan", "作业异常扫描"),
	SCAN_TASK_UNDONE("scan", "任务异常扫描"),
	SCAN_JOB_TIMEOUT("scan", "作业超时扫描"),
	SCAN_TASK_TIMEOUT("scan", "任务超时扫描"),
	SCAN_TASK_MISS("scan", "任务丢失扫描"),

	/**
	 * 统计分析类.
	 */
	STATIC_TASK_INFO("static", "任务作业统计分析"),
	STATIC_TASK_ECLAPSE("static", "任务耗时统计分析"),
	STATIC_SYSNO_INFO("static", "系统作业统计分析"),

	NONE("", "null");

/**
 * 组ID
 */
private String gid;

/**
 * 事件名称
 */
private String name;

private static String[] gids = null;

private EventType(String gid, String name) {
	this.gid = gid;
	this.name = name;
}

public String getGid() {
	return gid;
}

public void setGid(String gid) {
	this.gid = gid;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String[] gids() {
	if (gids == null) {
		List<String> l = new ArrayList<String>();
		for (EventType et : this.values()) {
			if (!l.contains(et.getGid())) {
				l.add(et.getGid());
			}
		}
		gids = (String[]) l.toArray((String[]) Array.newInstance(String.class, l.size()));
	}
	return gids;
}

public EventType[] values(String gid) {
	List<EventType> l = new ArrayList<EventType>();
	for (EventType et : this.values()) {
		if (et.getGid().equals(gid)) {
			l.add(et);
		}
	}
	return (EventType[]) l.toArray((EventType[]) Array.newInstance(EventType.class, l.size()));
}

public static void main(String[] args) {
	System.out.println(EventType.SCAN_JOB_TIMEOUT);
	// EventType.values()
}

}
