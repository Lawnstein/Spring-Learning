package com.iceps.spring.redis.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.ServiceState;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

/**
 * RocketMQ实现异步调用实现.
 * 
 * @author Lawnstein.Chan
 * @param <O>
 */
public class RmqAsyncCaller {
	protected final static Logger logger = LoggerFactory.getLogger(RmqAsyncCaller.class);

	final static ConcurrentSkipListSet concurrentConsumeMesgID = new ConcurrentSkipListSet();

	private final static int MAX_CONNECT_RETRY = 3;
	/**
	 * 消息队列URL，"/192.168.15.101:9876/系统代码/子系统代码"格式.<br>
	 * 例如: <br>
	 * RMQ：/$NamesrvAddr/$group/$topic".<br>
	 * AMQ：/$NamesrvAddr/$queue/$topic".<br>
	 */
	protected String mqURL;

	/**
	 * 异步服务执行端调用.
	 */
	protected TaskletExcute<Map, Void> taskletExcutor;

	/**
	 * 消费服务线程数.
	 */
	protected int defaultConsumerThreads;

	private String subExpression;

	private String addr;

	private String group;

	private String topic;

	private String tags;

	private int clientSize = 1;

	private int clientThreads = 0;

	private List<DefaultMQProducer> producers = new ArrayList<DefaultMQProducer>();

	private Random producerBase = new Random(System.currentTimeMillis());

	private ConcurrentHashMap<String, MQConsumer> consumerMap;

	private String seriCode = "KRYO";

	class MQConsumer {
		public DefaultMQPushConsumer c = null;

		public String trnCd = null;

		public AtomicLong r = new AtomicLong(0L);

		public boolean alive = true;

		public MQConsumer(DefaultMQPushConsumer consumer) {
			this.c = consumer;
		}

		public void shutdown() {
			if (c != null)
				c.shutdown();
			c = null;
		}

		public String toString() {
			return "MQConsumer [trnCd=" + trnCd + ", r=" + r.get() + ", c=" + c + "]";
		}

	}

	public String getMqURL() {
		return mqURL;
	}

	public TaskletExcute<Map, Void> getTaskletExcutor() {
		return taskletExcutor;
	}

	public void setTaskletExcutor(TaskletExcute<Map, Void> taskletExcutor) {
		this.taskletExcutor = taskletExcutor;
	}

	public int getDefaultConsumerThreads() {
		return defaultConsumerThreads;
	}

	public void setDefaultConsumerThreads(int defaultConsumerThreads) {
		this.defaultConsumerThreads = defaultConsumerThreads;
	}

	public RmqAsyncCaller() {
		super();
		consumerMap = new ConcurrentHashMap<String, MQConsumer>();
	}

	public void setMqURL(String mqURL) {
		this.mqURL = mqURL;
		String[] elems = mqURL.split("/");
		if (elems.length >= 1)
			addr = elems[1];
		if (elems.length >= 2)
			group = elems[2];
		if (elems.length >= 3)
			topic = elems[3];
		if (addr == null || addr.length() == 0)
			addr = "127.0.0.1:9876";
		if (group == null || group.length() == 0)
			group = "CBS";
		if (topic == null || topic.length() == 0)
			topic = "cbspe";
		logger.debug("MqURL: addr=" + addr + ", group=" + group + ", topic=" + topic);
	}

	public String getSubExpression() {
		return subExpression;
	}

	public void setSubExpression(String subExpression) {
		this.subExpression = subExpression;
	}

	public int getClientThreads() {
		return clientThreads;
	}

	public void setClientThreads(int clientThreads) {
		this.clientThreads = clientThreads;
	}

	public String getSeriCode() {
		return seriCode;
	}

	public void setSeriCode(String seriCode) {
		this.seriCode = seriCode.toUpperCase();
		if (!this.seriCode.equals("JDK") && !this.seriCode.equals("KRYO"))
			throw new RuntimeException("invalid seriCode value '" + seriCode + "', just surpport JDK,KRYO");
	}

	public int getClientSize() {
		return clientSize;
	}

	public void setClientSize(int clientSize) {
		this.clientSize = clientSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RmqAsyncCaller [subExpression=" + subExpression + ", addr=" + addr + ", group=" + group + ", topic="
				+ topic + ", tags=" + tags + ", producers=" + producers + ", clientThreads=" + clientThreads + "]";
	}

	public void onInit() {
		startProducer();
	}

	public void onClose() {
		if (producers.size() > 0) {
			for (DefaultMQProducer producer : producers) {
				producer.shutdown();
				logger.debug("RmqAsyncCaller producer " + producer + " was shutdown.");
			}
		}
		serverStop();
	}

	public void startProducer() {
		synchronized (mqURL) {
			if (producers.size() > 0)
				return;

			for (int n = 0; n < clientSize; n++) {
				DefaultMQProducer producer = null;
				logger.debug("try to start " + (n + 1) + "/" + clientSize + " producer ...");
				for (int ii = 0; ii < MAX_CONNECT_RETRY; ii++) {
					try {
						producer = new DefaultMQProducer("PRODUCER_" + group + "_" + topic + "_" + n);
						producer.setNamesrvAddr(addr);
						producer.setInstanceName("PRODUCER_" + this.group + "_"
								+ UUID.randomUUID().toString().replaceAll("-", "") + "_" + n);
						if (clientThreads > 0)
							producer.setClientCallbackExecutorThreads(clientThreads);
						producer.setRetryAnotherBrokerWhenNotStoreOK(true);
						producer.setRetryTimesWhenSendFailed(3);
						logger.debug(producer.toString());
						producer.start();

						while (true) {
							ServiceState serviceState = producer.getDefaultMQProducerImpl().getServiceState();
							logger.info("RmqAsyncCaller.Producer " + producer + " current state : " + serviceState);
							if (serviceState.equals(ServiceState.RUNNING)) {
								logger.debug("RmqAsyncCaller.Producer " + producer + " has start completely. ");
								break;
							} else if (serviceState.equals(ServiceState.SHUTDOWN_ALREADY)) {
								logger.warn("RmqAsyncCaller.Producer " + producer + " was SHUTDOWN_ALREADY. ");
								producer.shutdown();
								producer = null;
								break;
							} else if (serviceState.equals(ServiceState.START_FAILED)) {
								logger.warn("RmqAsyncCaller.Producer " + producer + " has start FAILED. ");
								producer.shutdown();
								producer = null;
								break;
							} else {
								try {
									Thread.currentThread().sleep(10);
								} catch (InterruptedException e) {
								}
							}
						}
						logger.info("RmqAsyncCaller.Producer " + producer + " started, sendMsgTimeout:"
								+ producer.getSendMsgTimeout() + ", ");
						break;
					} catch (MQClientException e) {
						if (producer != null)
							producer.shutdown();
						producer = null;

						if (ii < MAX_CONNECT_RETRY - 1)
							logger.warn("启动Producer异常:" + mqURL);
						else
							throw new RuntimeException("启动Producer异常.", e);
					}
				}

				if (producer != null)
					producers.add(producer);
			}
		}
	}

	private DefaultMQProducer getProducer() {
		if (producers == null || producers.size() == 0)
			startProducer();

		if (producers.size() > 0)
			return producers.get(producerBase.nextInt(producers.size()));
		return null;
	}

	private void closeProducer(DefaultMQProducer producer) {
		if (producer == null)
			return;
		if (producers != null && producers.contains(producer)) {
			producers.remove(producer);
			try {
				producer.shutdown();
			} catch (Exception e) {
				logger.warn("close the producer " + producer + " exception.", e);
			}
		}
	}

	public void startConsumer(String trnCd, int threads, TaskletExcute customerExcutor) {
		synchronized (consumerMap) {
			if (trnCd == null || trnCd.length() == 0)
				trnCd = "*";
			if (threads < 0)
				threads = defaultConsumerThreads;
			MQConsumer consumer1 = (MQConsumer) consumerMap.get(trnCd);
			if (consumer1 != null) {
				logger.warn("RmqAsyncCaller consumer " + trnCd + " has started, please check it : " + consumer1);
				return;
			}
			if (customerExcutor == null && taskletExcutor == null) {
				throw new RuntimeException("no taskletExcute configured for rmqAsyncCaller.");
			}
			final String Tags = trnCd;
			final String MesgTopic = topic + "_" + trnCd;
			final TaskletExcute currTaskletExcute = customerExcutor != null ? customerExcutor : taskletExcutor;
			for (int ii = 0; ii < MAX_CONNECT_RETRY; ii++) {
				final MQConsumer consumer = new MQConsumer(
						new DefaultMQPushConsumer("CONSUMER_" + group + "_" + MesgTopic));
				consumer.trnCd = Tags;
				consumer.c.setNamesrvAddr(addr);
				consumer.c.setInstanceName(
						"CONSUMER_" + this.group + "_" + UUID.randomUUID().toString().replaceAll("-", ""));
				try {
					consumer.c.subscribe(MesgTopic, Tags);
					logger.debug("RmqAsyncCaller subscribe Topic:" + MesgTopic + ", Tags:" + Tags + ", ConsumeThreads:"
							+ threads);
					consumer.c.setPullBatchSize(1);
					consumer.c.setConsumeThreadMin(threads);
					consumer.c.setConsumeThreadMax(threads);
					consumer.c.setClientCallbackExecutorThreads(threads < 4 ? 1
							: (threads / 2 > Runtime.getRuntime().availableProcessors()
									? Runtime.getRuntime().availableProcessors() : threads / 2));
					consumer.c.registerMessageListener(new MessageListenerConcurrently() {

						@Override
						public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
								ConsumeConcurrentlyContext arg1) {
							MessageExt msg = (msgs == null || msgs.size() == 0) ? null : msgs.get(0);
							if (msg == null)
								return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

							synchronized (consumer) {
								if (!consumer.alive) {
									return ConsumeConcurrentlyStatus.RECONSUME_LATER;
								}

								if (concurrentConsumeMesgID.contains(msg.getMsgId())) {
									logger.warn("Received MesgID " + msg.getMsgId() + " is on proccessing, ignore it.");
									return ConsumeConcurrentlyStatus.RECONSUME_LATER;
								} else {
									concurrentConsumeMesgID.add(msg.getMsgId());
								}

								consumer.r.getAndIncrement();
							}

							logger.trace("RmqAsyncCaller.Consumer recved : " + msg);
							logger.debug("RmqAsyncCaller.Consumer recved body : " + new String(msg.getBody()));

							/**
							 * get the Object.
							 */
							Object Object = null;
							try {
								if (logger.isDebugEnabled())
									logger.debug("seriCode " + seriCode);
								if (seriCode.equals("JDK")) {
									Object = JDKObjectSerializer.deserialize(msg.getBody());
								} else if (seriCode.equals("KRYO")) {
									Object = KryoObjectSerializer.deserialize(msg.getBody());
								}
							} catch (Throwable e) {
								logger.error(consumer + " deserialize the message with " + seriCode + " failed.",e);
							} finally {
							}
							logger.debug(
									"Topic:" + MesgTopic + ", Tags:" + Tags + ", Recved Object " + Object);

							/**
							 * excute the Object.
							 */
							try {

								if (Object != null) {
									currTaskletExcute.excute(Object);
								}
								logger.debug("Tasklet excute completely : " + Object);
							} catch (Throwable e) {
								logger.error("Tasklet excute failed : " + Object, e);
							}

							consumer.r.getAndDecrement();
							concurrentConsumeMesgID.remove(msg.getMsgId());
							return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
						}

					});
					consumer.c.start();
					logger.info("Listened on Consumer(" + MesgTopic + "," + Tags + ") " + consumer);
					consumerMap.put(trnCd, consumer);
					break;
				} catch (MQClientException e) {
					if (consumer.c != null)
						consumer.c.shutdown();

					if (ii < MAX_CONNECT_RETRY - 1)
						logger.warn("启动Consumer异常:" + mqURL);
					else
						throw new RuntimeException("启动Consumer异常.", e);
				}
			}
		}
	}


	public boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	public void clientCall(Map Object) {
		Message msg = null;
		String mesgID = null;
		try {
			byte[] sendBytes = null;
			if (logger.isDebugEnabled())
				logger.debug("seriCode " + seriCode);
			if (seriCode.equals("JDK"))
				sendBytes = JDKObjectSerializer.serialize(Object);
			else if (seriCode.equals("KRYO"))
				sendBytes = KryoObjectSerializer.serialize(Object);

			mesgID = UUID.randomUUID().toString().replaceAll("-", "");
			String tags = (String) Object.get("TrnCd");
			if (isEmpty(tags))
				tags = (String) Object.get("trnCd");
			if (isEmpty(tags))
				tags = "CBS";

			String mesgTopic = topic + "_" + tags;
			if (logger.isDebugEnabled())
				logger.debug("send Topic:" + mesgTopic + ", Tags:" + tags + ", MesgID:" + mesgID + ", sendBytes("
						+ sendBytes.length + "):" + new String(sendBytes));

			msg = new Message(mesgTopic, tags, mesgID, sendBytes);
		} catch (Throwable e) {
			throw new RuntimeException("Generate message failed.", e);
		}

		DefaultMQProducer producer = null;
		RuntimeException cte = null;
		SendResult sendResult = null;
		for (int i = 0; i < 3; i++) {
			producer = getProducer();
			if (producer == null)
				throw new RuntimeException("Get connection failed.", null);

			try {
				sendResult = producer.send(msg);
				// SendStat sendStat = sendResult.getSendStat();
				// sendStat.SEND_OK;
				// sendStat.SLAVE_NOT_AVAILABLE;
				// sendStat.FLUSH_DISK_TIMEOUT;
				// sendStat.FLUSH_SLAVE_TIMEOUT;
				logger.debug("[" + mesgID + ":" + i + "] Send Message Result " + sendResult);
				cte = null;
				break;
			} catch (MQClientException e) {
				logger.error("[" + mesgID + ":" + i + "] Send Message Result " + sendResult
						+ ", catch MQClientException: RocketMQ.ResponseCode:" + e.getResponseCode() + ", ErrorMessage:"
						+ e.getErrorMessage());
				closeProducer(producer);
				cte = new RuntimeException("Send message with MQClientException", e);
			} catch (RemotingException e) {
				logger.error("[" + mesgID + ":" + i + "] Send Message Result " + sendResult
						+ ", catch RemotingException" + e);
				closeProducer(producer);
				cte = new RuntimeException("Send message with RemotingException", e);
			} catch (MQBrokerException e) {
				logger.error("[" + mesgID + ":" + i + "] Send Message Result " + sendResult
						+ ", catch MQBrokerException: RocketMQ.ResponseCode:" + e.getResponseCode() + ", ErrorMessage:"
						+ e.getErrorMessage());
				closeProducer(producer);
				cte = new RuntimeException("Send message with MQBrokerException", e);
			} catch (InterruptedException e) {
				logger.error("[" + mesgID + ":" + i + "] Send Message Result " + sendResult
						+ ", catch InterruptedException.");
				cte = new RuntimeException("Send message with InterruptedException", e);
				break;
			}
		}
		if (cte != null)
			throw cte;
	}

	/**
	 * 处理一个异步任务.
	 * 
	 * @param Object
	 * @throws Exception
	 */
	public void excute(Map Object) {
		taskletExcutor.excute(Object);
	}

	public void serverStart() {
		startConsumer(null, getDefaultConsumerThreads(), null);

	}

	public void serverStart(final String trnCd, final int consumerThreads, final TaskletExcute customerExcutor) {
		startConsumer(trnCd, consumerThreads, customerExcutor);

	}

	public void serverStop() {
		while (consumerMap != null && consumerMap.size() > 0) {
			Iterator<String> iter = consumerMap.keySet().iterator();
			while (iter.hasNext()) {
				String k = (String) iter.next();
				MQConsumer consumer = consumerMap.get(k);
				consumer.alive = false;
				if (consumer.r.get() == 0) {
					synchronized (consumer) {
						if (consumer.r.get() == 0) {
							consumer.shutdown();
							consumerMap.remove(k);
							logger.debug("RmqAsyncCaller consumer " + consumer + " was shutdown.");
						} else {
							logger.warn("RmqAsyncCaller consumer " + consumer + " is running, waiting for it.");
						}
					}
				} else {
					logger.warn("RmqAsyncCaller consumer " + consumer + " is running, waiting for it.");
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		logger.debug("All the consumer closed : " + consumerMap.size());
	}

}
