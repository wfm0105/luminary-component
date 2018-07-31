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
	
	protected RedisOperator redisOperate;
	
	protected int minExpiredSeconds = ExpiredConstants.MIN_EXPIRED_SECONDS;
	
	protected int maxExpiredSeconds = ExpiredConstants.MAX_EXPIRED_SECONDS;
	
	public RedisCacheProcessor(String key) {
		super(key);
	}
	
	public RedisCacheProcessor(RedisOperator redisOperate) {
		this.expiredStrategy = new BaseExpiredStrategy(minExpiredSeconds, maxExpiredSeconds);
		this.redisOperate = redisOperate;
	}
	
	public RedisCacheProcessor(String key, RedisOperator redisOperate) {
		super(key);
		this.expiredStrategy = new BaseExpiredStrategy(minExpiredSeconds, maxExpiredSeconds);
		this.redisOperate = redisOperate;
	}
	
	public RedisCacheProcessor(RedisOperator redisOperate, ExpiredStrategy expiredStrategy) {
		this.expiredStrategy = expiredStrategy;
		this.redisOperate = redisOperate;
	}
	
	public RedisCacheProcessor(String key, RedisOperator redisOperate, ExpiredStrategy expiredStrategy) {
		super(key);
		this.expiredStrategy = expiredStrategy;
		this.redisOperate = redisOperate;
	}
	
	public RedisCacheProcessor(String key, boolean disableAll, String[] disableKeys) {
		super(key);
		this.disableAll = disableAll;
		this.disableKeys = disableKeys;
	}
	
	public RedisCacheProcessor(RedisOperator redisOperate, boolean disableAll, String[] disableKeys) {
		this.expiredStrategy = new BaseExpiredStrategy(minExpiredSeconds, maxExpiredSeconds);
		this.redisOperate = redisOperate;
		this.disableAll = disableAll;
		this.disableKeys = disableKeys;
	}
	
	public RedisCacheProcessor(String key, RedisOperator redisOperate, boolean disableAll, String[] disableKeys) {
		super(key);
		this.expiredStrategy = new BaseExpiredStrategy(minExpiredSeconds, maxExpiredSeconds);
		this.redisOperate = redisOperate;
		this.disableAll = disableAll;
		this.disableKeys = disableKeys;
	}
	
	public RedisCacheProcessor(RedisOperator redisOperate, ExpiredStrategy expiredStrategy, boolean disableAll, String[] disableKeys) {
		this.expiredStrategy = expiredStrategy;
		this.redisOperate = redisOperate;
		this.disableAll = disableAll;
		this.disableKeys = disableKeys;
	}
	
	public RedisCacheProcessor(String key, RedisOperator redisOperate, ExpiredStrategy expiredStrategy, boolean disableAll, String[] disableKeys) {
		super(key);
		this.expiredStrategy = expiredStrategy;
		this.redisOperate = redisOperate;
		this.disableAll = disableAll;
		this.disableKeys = disableKeys;
	}
	
	@Override
	public boolean simpleCache(String keyPrefix, String key, String data) {
		int offset = HashAlgorithms.BKDRHash(key);
		logger.info("RedisCacheProcesser.cache:keyPrefix="+keyPrefix+",key="+key+",offset="+offset);
		redisOperate.setBit(mapKey(keyPrefix), offset, true);
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
		logger.info("RedisCacheProcesser.cache:key="+key+",offset="+offset);
		redisOperate.setBit(mapKey(key), offset, true);
		
		logger.info("RedisCacheProcesser.cache:key="+key+",value="+data);
		redisOperate.set(key, data, expiredStrategy.expiredSeconds());
		return true;
	}
	
	public boolean doCache(String keyPrefix, String key, String data, ExpiredStrategy expiredStrategy) {
		int offset = HashAlgorithms.BKDRHash(key);
		logger.info("RedisCacheProcesser.cache:keyPrefix="+keyPrefix+",key="+key+",offset="+offset);
		redisOperate.setBit(mapKey(keyPrefix), offset, true);
		
		logger.info("RedisCacheProcesser.cache:key="+key+",value="+data);
		redisOperate.set(key, data, expiredStrategy.expiredSeconds());
		return true;
	}

	@Override
	public boolean isValid(String keyPrefix, String key) {
		int offset = HashAlgorithms.BKDRHash(key);
		logger.info("RedisCacheProcesser.isValid:keyPrefix="+keyPrefix+"key="+key+",offset="+offset);
		boolean isCachedByBitMap = redisOperate.getBit(mapKey(keyPrefix), offset);
		logger.info("RedisCacheProcesser.isValid by bitmap:"+isCachedByBitMap);
		return isCachedByBitMap;
	}
	
	@Override
	public String getDataFromCache(String key) {
		return redisOperate.get(key);
	}
	
	@Override
	public boolean deleteCache(String key) {
		int offset = HashAlgorithms.BKDRHash(key);
		logger.info("RedisCacheProcesser.deleteCache:key="+key+",offset="+offset);
		//redisOperate.setBit(mapKey(key), offset, false);
		redisOperate.del(key);
		return true;
	}
	
	private String mapKey(String key) {
		return key+BITMAP_KEY_SYMBOL;
	}
	
	public RedisOperator getRedisOperate() {
		return redisOperate;
	}

	public void setRedisOperate(RedisOperator redisOperate) {
		this.redisOperate = redisOperate;
	}

	public ExpiredStrategy getExpiredStrategy() {
		return expiredStrategy;
	}

	public void setExpiredStrategy(ExpiredStrategy expiredStrategy) {
		this.expiredStrategy = expiredStrategy;
	}

	public static void main(String[] args) {
		System.out.println(HashAlgorithms.BKDRHash("wulinfeng"));
		System.out.println(HashAlgorithms.BKDRHash("sfesfesf"));
	}

}
