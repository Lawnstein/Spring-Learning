
package com.iceps.spring.disruptor.lock;

/**
 * 分布式任务锁.
 * 
 * @author Lawnstein.Chan
 * @version $Revision:$
 */
public interface TaskletLock {

	/**
	 * 阻塞获取key锁后,再执行tasklet,执行完毕后释放该锁.
	 * 
	 * @param key
	 * @param tasklet
	 * @return
	 */
	public boolean lock(String key, Runnable tasklet);

	/**
	 * 阻塞获取key锁(加锁超时则直接返回),然后再执行tasklet,执行完毕后释放该锁.
	 * 
	 * @param key
	 * @param timeoutSec
	 * @param tasklet
	 * @return
	 */
	public boolean lock(String key, long timeoutSec, Runnable tasklet);


	/**
	 * 阻塞获取key锁后,再执行tasklet,执行完毕后释放该锁.
	 * 
	 * @param key
	 * @param lockSec
	 * @return
	 */
	public boolean lock(String key, long lockSec);

	/**
	 * 阻塞获取key锁(加锁超时则直接返回),然后再执行tasklet,执行完毕后释放该锁.
	 * 
	 * @param key
	 * @param timeoutSec
	 * @param lockSec
	 * @return
	 */
	public boolean lock(String key, long timeoutSec, long lockSec);


}
