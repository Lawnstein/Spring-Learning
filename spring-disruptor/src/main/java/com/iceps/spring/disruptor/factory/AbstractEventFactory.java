package com.iceps.spring.disruptor.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import com.iceps.spring.disruptor.constant.EventObject;
import com.iceps.spring.disruptor.constant.EventType;
import com.iceps.spring.disruptor.service.EventHandler;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 事件调度工厂.
 * 
 * @author Lawnstein.Chan
 * @version $Revision:$
 */
public abstract class AbstractEventFactory implements ApplicationContextAware, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(AbstractEventFactory.class);

	protected ApplicationContext applicationContext;

	/**
	 * 默认事件池大小.
	 */
	protected final static int DEFAULT_RINGBUFFERSIZE = 1024 * 1024;

	/**
	 * 事件队列大小.
	 */
	protected Map<String, Integer> ringBufferSizes;

	/**
	 * 默认线程池大小.
	 */
	protected final static int DEFAULT_THREADPOOLSIZE = (int) Runtime.getRuntime().availableProcessors();

	/**
	 * 最小事件并发处理线程池.
	 */
	protected Map<String, Integer> coreThreadPoolSizes;

	/**
	 * 最大事件并发处理线程池.
	 */
	protected Map<String, Integer> maxThreadPoolSizes;

	/**
	 * 事件处理器
	 */
	protected Map<EventType, EventHandler> eventHandlers;

	/**
	 * 调度器
	 */
	protected Map<String, RingBuffer<EventObject>> eventDisruptors;

	protected Map<String, ExecutorService> eventExecutors;

	protected Map<String, WorkerPool> eventWorkerPools;

	protected List<InnerEventProducer> eventProducers;

	protected AtomicInteger eventProduceri;

	protected final static int DEFAULT_PRODUCERSIZE = DEFAULT_THREADPOOLSIZE;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public Map<String, Integer> getCoreThreadPoolSizes() {
		return coreThreadPoolSizes;
	}

	public void setCoreThreadPoolSizes(Map<String, Integer> coreThreadPoolSizes) {
		this.coreThreadPoolSizes = coreThreadPoolSizes;
	}

	public Map<String, Integer> getMaxThreadPoolSizes() {
		return maxThreadPoolSizes;
	}

	public void setMaxThreadPoolSizes(Map<String, Integer> maxThreadPoolSizes) {
		this.maxThreadPoolSizes = maxThreadPoolSizes;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (ringBufferSizes == null) {
			ringBufferSizes = new HashMap<String, Integer>();
		}
		if (coreThreadPoolSizes == null) {
			coreThreadPoolSizes = new HashMap<String, Integer>();
		}
		if (maxThreadPoolSizes == null) {
			maxThreadPoolSizes = new HashMap<String, Integer>();
		}

		Map<String, EventHandler> handlers = applicationContext.getBeansOfType(EventHandler.class);
		eventHandlers = new HashMap<EventType, EventHandler>();
		eventDisruptors = new HashMap<String, RingBuffer<EventObject>>();
		if (handlers != null && handlers.size() > 0) {
			for (Entry<String, EventHandler> e : handlers.entrySet()) {
				if (eventHandlers.containsKey(e.getValue().getEventType())) {
					logger.warn("Initial EventHandlers warning: duplicated event handler {} for EventType {}",
							e.getKey(), e.getValue().getEventType());
				} else {
					eventHandlers.put(e.getValue().getEventType(), e.getValue());
					logger.info("Initial EventHandler {} for EventType {}", e.getKey(), e.getValue().getEventType());
				}

				if (!eventDisruptors.containsKey(e.getValue().getEventType().getGid())) {
					eventDisruptors.put(e.getValue().getEventType().getGid(), null);
					logger.info("Initial EventDisruptor group {} ", e.getValue().getEventType().getGid());
				}
			}
		}
	}

	public void start() {
		Assert.notNull(eventDisruptors, "eventDisruptors cannot be null");
		for (Entry<String, RingBuffer<EventObject>> e : eventDisruptors.entrySet()) {
			int ringBufferSize = ringBufferSizes.getOrDefault(e.getKey(), DEFAULT_RINGBUFFERSIZE);
			int corePoolSize = coreThreadPoolSizes.getOrDefault(e.getKey(), DEFAULT_THREADPOOLSIZE);
			int maxPoolSize = maxThreadPoolSizes.getOrDefault(e.getKey(), DEFAULT_THREADPOOLSIZE);
			if (maxPoolSize < corePoolSize) {
				maxPoolSize = corePoolSize;
			}
			if (eventExecutors == null) {
				synchronized (eventDisruptors) {
					if (eventExecutors == null) {
						eventExecutors = new ConcurrentHashMap<String, ExecutorService>();
					}
				}
			}
			ExecutorService es = initializeExecutor(e.getKey(), corePoolSize, maxPoolSize, 10 * 60);
			eventExecutors.put(e.getKey(), es);

			///////////////////////////////////////////////////////////////////
			// Disruptor<EventObject> disruptor = new
			// Disruptor<EventObject>(EventObject::new, ringBufferSize, es,
			// ProducerType.MULTI, new BlockingWaitStrategy());
			// disruptor.handleEventsWith(this);
			// eventDisruptors.put(e.getKey(), disruptor);
			// disruptor.start();

			// 创建ringBuffer
			/**
			 * BlockingWaitStrategy
			 * 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现.<br>
			 * SleepingWaitStrategy
			 * 的性能表现跟BlockingWaitStrategy差不多，对CPU的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景.<br>
			 * YieldingWaitStrategy
			 * 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于CPU逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性.<br>
			 */
			RingBuffer<EventObject> ringBuffer = RingBuffer.create(ProducerType.MULTI,
					new com.lmax.disruptor.EventFactory<EventObject>() {
						@Override
						public EventObject newInstance() {
							return new EventObject();
						}
					}, ringBufferSize, new BlockingWaitStrategy());
			eventDisruptors.put(e.getKey(), ringBuffer);
			InnerEventWorkHandler handlers[] = new InnerEventWorkHandler[corePoolSize];
			for (int j = 0; j < corePoolSize; j++) {
				handlers[j] = new InnerEventWorkHandler(this);
			}
			WorkerPool<EventObject> workerPool = new WorkerPool<EventObject>(ringBuffer, ringBuffer.newBarrier(),
					new InnerEventExceptionHandler(), handlers);
			if (eventWorkerPools == null) {
				synchronized (eventDisruptors) {
					if (eventWorkerPools == null) {
						eventWorkerPools = new ConcurrentHashMap<String, WorkerPool>();
					}
				}
			}
			eventWorkerPools.put(e.getKey(), workerPool);
			ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
			workerPool.start(es);

			///////////////////////////////////////////////////////////////////
			// Disruptor<EventObject> disruptor = new
			// Disruptor<EventObject>(EventObject::new, ringBufferSize,
			// new NamedThreadFactory(e.getKey()));
			// disruptor.handleEventsWith(new EventFactory<EventObject>() {
			// @Override
			// public EventObject newInstance() {
			// return new EventObject();
			// }
			// }));
			// eventDisruptors.put(e.getKey(), disruptor);
			// disruptor.start();

			logger.info("Disruptor {} started: ringBufferSize {}, coreThreadPoolSize {}, maxThreadPoolSize {}",
					e.getKey(), ringBufferSize, corePoolSize, maxPoolSize);
		}
	}

	public void stop() {
		if (eventDisruptors == null) {
			return;
		}
		for (Entry<String, RingBuffer<EventObject>> e : eventDisruptors.entrySet()) {
			RingBuffer rb = e.getValue();
			if (rb == null) {
				continue;
			}

			if (eventWorkerPools.get(e.getKey()) != null && eventWorkerPools.get(e.getKey()).isRunning())
				eventWorkerPools.get(e.getKey()).halt();
			if (eventExecutors.get(e.getKey()) != null && !eventExecutors.get(e.getKey()).isTerminated())
				eventExecutors.get(e.getKey()).shutdown();

			eventDisruptors.put(e.getKey(), null);
			logger.info("Disruptor {} stoped", e.getKey());
		}
	}

	protected void fireEvent(EventType eventType, Object eventArgs) {
		if (eventProducers == null) {
			synchronized (eventDisruptors) {
				if (eventProducers == null) {
					eventProduceri = new AtomicInteger(0);
					eventProducers = new ArrayList<InnerEventProducer>(DEFAULT_PRODUCERSIZE);
					for (int i = 0; i < DEFAULT_PRODUCERSIZE; i++) {
						eventProducers.add(new InnerEventProducer(this));
					}
				}
				
			}
		}
		int p = eventProduceri.getAndIncrement();
		eventProducers.get(p % DEFAULT_PRODUCERSIZE).onData(eventType, eventArgs);
	}

	protected ExecutorService initializeExecutor(String threadNamePrefix, int corePoolSize, int maxPoolSize,
			int keepAliveSeconds) {
		// return Executors.newCachedThreadPool();
		// return Executors.newFixedThreadPool(corePoolSize);
		// return Executors.newFixedThreadPool(corePoolSize, new
		// NamedThreadFactory(threadNamePrefix));
		return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(), new NamedThreadFactory(threadNamePrefix), new AbortPolicy());
	}

	static class InnerEventExceptionHandler implements com.lmax.disruptor.ExceptionHandler {
		public void handleEventException(Throwable ex, long sequence, Object event) {
			logger.error("caught EventException {}, sequence {}, event {}.", ex, sequence, event);
		}

		public void handleOnStartException(Throwable ex) {
			logger.error("caught OnStartException {}", ex);
		}

		public void handleOnShutdownException(Throwable ex) {
			logger.error("caught OnShutdownException {}", ex);
		}
	}

	static class InnerEventHandler implements com.lmax.disruptor.EventHandler<EventObject> {
		private AbstractEventFactory factory;

		public InnerEventHandler(AbstractEventFactory abstractEventFactory) {
			this.factory = abstractEventFactory;
		}

		@Override
		public void onEvent(EventObject event, long sequence, boolean endOfBatch) throws Exception {
			if (event instanceof EventObject) {
				EventObject eo = (EventObject) event;
				EventHandler eh = factory.eventHandlers.get(eo.getEventType());
				if (eh == null) {
					logger.warn("unexpected event, no handler implemented for it. Event {}, sequence {}, endOfBatch {}",
							event, sequence, endOfBatch);
					return;
				}
				logger.debug("fire event {}, sequence {}, endOfBatch {}", event, sequence, endOfBatch);
				eh.handle(eo);
			} else {
				logger.warn("unexpected event {}, sequence {}, endOfBatch {}", event, sequence, endOfBatch);
			}
		}
	}

	static class InnerEventWorkHandler implements com.lmax.disruptor.WorkHandler<EventObject> {
		private AbstractEventFactory factory;

		public InnerEventWorkHandler(AbstractEventFactory abstractEventFactory) {
			this.factory = abstractEventFactory;
		}

		@Override
		public void onEvent(EventObject event) throws Exception {
			if (event instanceof EventObject) {
				EventObject eo = (EventObject) event;
				EventHandler eh = factory.eventHandlers.get(eo.getEventType());
				if (eh == null) {
					logger.warn("unexpected event, no handler implemented for it. {} ", event);
					return;
				}
				logger.debug("fire event {}", event);
				eh.handle(eo);
			} else {
				logger.warn("unexpected event {}", event);
			}
		}
	}

	static class InnerEventProducer {
		private AbstractEventFactory factory;

		public InnerEventProducer(AbstractEventFactory abstractEventFactory) {
			this.factory = abstractEventFactory;
		}

		public void onData(EventType eventType, Object eventArgs) {
			Assert.notNull(eventType, "eventType cannot be null");
			Assert.notNull(factory, "factory cannot be null");
			Assert.notNull(factory.eventDisruptors, "eventDisruptor cannot be null");

			String gid = eventType.getGid();
			RingBuffer<EventObject> rb = factory.eventDisruptors.get(gid);
			if (rb == null) {
				logger.error("unexpected EventType, no disruptor found for {}", eventType);
				return;
			}
			// RingBuffer<EventObject> rb = d.getRingBuffer();
			long sequence = rb.next();
			EventObject e = null;
			try {
				e = rb.get(sequence);
				e.setEventType(eventType);
				e.setEventArgs(eventArgs);
			} catch (Throwable th) {
				logger.error("fireEvent {} failed.", eventType, th);
			} finally {
				rb.publish(sequence);
				logger.info("fired {}", e);
			}
		}
	}
}
