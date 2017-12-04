package com.iceps.spring.redis.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

public class RedisSequence {

	protected final static Logger logger = LoggerFactory.getLogger(RedisSequence.class);

	private final String PATH_ENTRY_CONF = "/SEQUENCE/CONF/";
	private final String PATH_ENTRY_VAL = "/SEQUENCE/VAL/";
	private final String KEY_STEP = "_STEP";
	private final String KEY_STARTWITH = "_STARTWITH";
	private final String KEY_CURRVAL = "_CURRVAL";
	private final String KEY_MAX = "_MAX";
	private final String KEY_CACHE = "_CACHE";

	private RedisOperations<String, Long> generalOps;
	private ValueOperations<String, Long> valueOperations;
	private HashOperations<String, String, Long> hashOperations;
	private Map<String, Map<String, Long>> seqSchemas = new ConcurrentHashMap<String, Map<String, Long>>();

	public RedisSequence() {
	}

	public RedisSequence(RedisConnectionFactory connectionFactory) {
		super();
		setConnectionFactory(connectionFactory);
	}

	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		Assert.notNull(connectionFactory, "a valid connectionFactory is required");

		RedisTemplate<String, Long> redisTemplate = new RedisTemplate<String, Long>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericToStringSerializer<Long>(Long.class));
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new GenericToStringSerializer<Long>(Long.class));
		redisTemplate.setExposeConnection(true);
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.afterPropertiesSet();

		// this.key = redisCounter;
		generalOps = redisTemplate;
		valueOperations = generalOps.opsForValue();
		hashOperations = generalOps.opsForHash();

	}

	private long get(String key) {
		return valueOperations.get(key);
	}

	public void set(String key, long newValue) {
		valueOperations.set(key, newValue);
	}

	private void setIfAbsent(String key, long newValue) {
		valueOperations.setIfAbsent(key, newValue);
	}

	private long incrementAndGet(String key, long step) {
		return valueOperations.increment(key, step);
	}

	private Map<String, Long> schema(String sequenceName, long step, long startWith) {
		Map<String, Long> m = seqSchemas.get(sequenceName);
		if (m == null) {
			m = hashOperations.entries(PATH_ENTRY_CONF + sequenceName);
			if (m == null || m.size() == 0) {
				synchronized (seqSchemas) {
					m = hashOperations.entries(PATH_ENTRY_CONF + sequenceName);
					if (m == null || m.size() == 0) {
						m = new HashMap();
						m.put(KEY_STARTWITH, new Long(startWith));
						// m.put(KEY_CURRVAL, new Long(startWith - 1));
						m.put(KEY_STEP, new Long(step));
						m.put(KEY_MAX, new Long(9999999999L));
						m.put(KEY_CACHE, new Long(0));
						hashOperations.putAll(PATH_ENTRY_CONF + sequenceName, m);
						setIfAbsent(PATH_ENTRY_VAL + sequenceName, new Long(startWith - 1));
						seqSchemas.put(sequenceName, m);
					}
				}
			}
		}
		return m;
	}

	public long currval(String sequenceName) {
		Map<String, Long> m = schema(sequenceName, 1, 1);
		return get(PATH_ENTRY_VAL + sequenceName);
	}

	public long nextval(String sequenceName, long step, long startWith) {
		Map<String, Long> m = schema(sequenceName, step, startWith);
		long v = incrementAndGet(PATH_ENTRY_VAL + sequenceName, m.get(KEY_STEP).longValue());
		if (v > m.get(KEY_MAX)) {
			reset(sequenceName);
			v = incrementAndGet(PATH_ENTRY_VAL + sequenceName, m.get(KEY_STEP).longValue());
		}
		return v;
	}

	public void reset(String sequenceName) {
		reset(sequenceName, 1, 1);
	}

	public void reset(String sequenceName, long step, long startWith) {
		Map<String, Long> m = schema(sequenceName, step, startWith);
		hashOperations.put(PATH_ENTRY_CONF + sequenceName, KEY_CURRVAL, m.get(KEY_STARTWITH) - 1);
	}

}
