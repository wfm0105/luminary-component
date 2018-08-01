/**  
* <p>Title: RedisCacheProcessor.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午4:28:34  
*/  
package com.luminary.component.cache.processor.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luminary.component.cache.algorithms.HashAlgorithms;
import com.luminary.component.cache.expired.BaseExpiredStrategy;
import com.luminary.component.cache.expired.ExpiredConstants;
import com.luminary.component.cache.expired.ExpiredStrategy;
import com.luminary.component.cache.operator.redis.RedisOperator;
import com.luminary.component.cache.processor.AbstractCacheProcessor;

/**  
* <p>Title: RedisCacheProcessor</p>  
* <p>Description: 基于redis的缓存处理类</p>  
* @author wulinfeng
* @date 2018年7月31日下午4:28:34
*/
public class RedisCacheProcessor extends AbstractCacheProcessor{

	Logger logger = LoggerFactory.getLogger(RedisCacheProcessor.class);
	
	private static final String BITMAP_KEY_SYMBOL = "_bitMap";
	
	//private JedisPoolConfig jedisPoolConfig;
	
	protected ExpiredStrategy expiredStrategy;
	
//	private String host;
//	
//	private int port;
//	
//	private String password;
//	
//	private JedisUtil jedisUtil;
	
	protected RedisOperator redisOperator;
	
	protected int minExpiredSeconds = ExpiredConstants.MIN_EXPIRED_SECONDS;
	
	protected int maxExpiredSeconds = ExpiredConstants.MAX_EXPIRED_SECONDS;
	
	public RedisCacheProcessor(String key) {
		super(key);
	}
	
	public RedisCacheProcessor(RedisOperator redisOperator) {
		this.expiredStrategy = new BaseExpiredStrategy(minExpiredSeconds, maxExpiredSeconds);
		this.redisOperator = redisOperator;
	}
	
	public RedisCacheProcessor(String key, RedisOperator redisOperator) {
		super(key);
		this.expiredStrategy = new BaseExpiredStrategy(minExpiredSeconds, maxExpiredSeconds);
		this.redisOperator = redisOperator;
	}
	
	public RedisCacheProcessor(RedisOperator redisOperator, ExpiredStrategy expiredStrategy) {
		this.expiredStrategy = expiredStrategy;
		this.redisOperator = redisOperator;
	}
	
	public RedisCacheProcessor(String key, RedisOperator redisOperator, ExpiredStrategy expiredStrategy) {
		super(key);
		this.expiredStrategy = expiredStrategy;
		this.redisOperator = redisOperator;
	}
	
	public RedisCacheProcessor(String key, boolean disableAll, String[] disableKeys) {
		super(key);
		this.disableAll = disableAll;
		this.disableKeys = disableKeys;
	}
	
	public RedisCacheProcessor(RedisOperator redisOperator, boolean disableAll, String[] disableKeys) {
		this.expiredStrategy = new BaseExpiredStrategy(minExpiredSeconds, maxExpiredSeconds);
		this.redisOperator = redisOperator;
		this.disableAll = disableAll;
		this.disableKeys = disableKeys;
	}
	
	public RedisCacheProcessor(String key, RedisOperator redisOperator, boolean disableAll, String[] disableKeys) {
		super(key);
		this.expiredStrategy = new BaseExpiredStrategy(minExpiredSeconds, maxExpiredSeconds);
		this.redisOperator = redisOperator;
		this.disableAll = disableAll;
		this.disableKeys = disableKeys;
	}
	
	public RedisCacheProcessor(RedisOperator redisOperator, ExpiredStrategy expiredStrategy, boolean disableAll, String[] disableKeys) {
		this.expiredStrategy = expiredStrategy;
		this.redisOperator = redisOperator;
		this.disableAll = disableAll;
		this.disableKeys = disableKeys;
	}
	
	public RedisCacheProcessor(String key, RedisOperator redisOperator, ExpiredStrategy expiredStrategy, boolean disableAll, String[] disableKeys) {
		super(key);
		this.expiredStrategy = expiredStrategy;
		this.redisOperator = redisOperator;
		this.disableAll = disableAll;
		this.disableKeys = disableKeys;
	}
	
	@Override
	public boolean simpleCache(String keyPrefix, String key, String data) {
		int offset = HashAlgorithms.BKDRHash(key);
		logger.info("RedisCacheProcessor.cache:keyPrefix="+keyPrefix+",key="+key+",offset="+offset);
		redisOperator.setBit(mapKey(keyPrefix), offset, true);
		return true;
	}
	
	@Override
	public boolean cache(String key, String data) {
		return doCache(key, data, this.expiredStrategy);
	}
	
	@Override
	public boolean cache(String keyPrefix, String key, String data) {
		return doCache(keyPrefix, key, data, this.expiredStrategy);
	}
	
	@Override
	public boolean cache(String key, String data, ExpiredStrategy expiredStrategy) {
		return doCache(key, data, expiredStrategy);
	}
	
	@Override
	public boolean cache(String keyPrefix, String key, String data, ExpiredStrategy expiredStrategy) {
		return doCache(keyPrefix, key, data, expiredStrategy);
	}
	
	private boolean doCache(String key, String data, ExpiredStrategy expiredStrategy) {
		int offset = HashAlgorithms.BKDRHash(key);
		logger.info("RedisCacheProcessor.cache:key="+key+",offset="+offset);
		redisOperator.setBit(mapKey(key), offset, true);
		
		logger.info("RedisCacheProcessor.cache:key="+key+",value="+data);
		redisOperator.set(key, data, expiredStrategy.expiredSeconds());
		return true;
	}
	
	public boolean doCache(String keyPrefix, String key, String data, ExpiredStrategy expiredStrategy) {
		int offset = HashAlgorithms.BKDRHash(key);
		logger.info("RedisCacheProcessor.cache:keyPrefix="+keyPrefix+",key="+key+",offset="+offset);
		redisOperator.setBit(mapKey(keyPrefix), offset, true);
		
		logger.info("RedisCacheProcessor.cache:key="+key+",value="+data);
		redisOperator.set(key, data, expiredStrategy.expiredSeconds());
		return true;
	}

	@Override
	public boolean isValid(String keyPrefix, String key) {
		int offset = HashAlgorithms.BKDRHash(key);
		logger.info("RedisCacheProcessor.isValid:keyPrefix="+keyPrefix+"key="+key+",offset="+offset);
		boolean isCachedByBitMap = redisOperator.getBit(mapKey(keyPrefix), offset);
		logger.info("RedisCacheProcessor.isValid by bitmap:"+isCachedByBitMap);
		return isCachedByBitMap;
	}
	
	@Override
	public String getDataFromCache(String key) {
		return redisOperator.get(key);
	}
	
	@Override
	public boolean deleteCache(String key) {
		int offset = HashAlgorithms.BKDRHash(key);
		logger.info("RedisCacheProcessor.deleteCache:key="+key+",offset="+offset);
		//redisOperator.setBit(mapKey(key), offset, false);
		redisOperator.del(key);
		return true;
	}
	
	private String mapKey(String key) {
		return key+BITMAP_KEY_SYMBOL;
	}
	
	public RedisOperator getRedisOperator() {
		return redisOperator;
	}

	public void setRedisOperator(RedisOperator redisOperator) {
		this.redisOperator = redisOperator;
	}

	public ExpiredStrategy getExpiredStrategy() {
		return expiredStrategy;
	}

	public void setExpiredStrategy(ExpiredStrategy expiredStrategy) {
		this.expiredStrategy = expiredStrategy;
	}

}
