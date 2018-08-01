/**  
* <p>Title: JedisRedisCacheProcessor.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午4:28:20  
*/  
package com.luminary.component.cache.processor.redis;

import com.luminary.component.cache.config.CacheConfiguration;
import com.luminary.component.cache.expired.ExpiredStrategy;
import com.luminary.component.cache.operator.redis.JedisRedisOperator;

import redis.clients.jedis.JedisPoolConfig;

/**  
* <p>Title: JedisRedisCacheProcessor</p>  
* <p>Description: 基于jedis的redis缓存操作类</p>  
* @author wulinfeng
* @date 2018年7月31日下午4:28:20
*/
public class JedisRedisCacheProcessor extends RedisCacheProcessor{

	public JedisRedisCacheProcessor(String key, String host, int port, String password) {
		super(key, CacheConfiguration.wrapRedisOperator(new JedisRedisOperator(host, port, password)));
	}
	
	public JedisRedisCacheProcessor(String key, JedisPoolConfig jedisPoolConfig, String host, int port, String password) {
		super(key, CacheConfiguration.wrapRedisOperator(new JedisRedisOperator(host, port, password, jedisPoolConfig)));
	}
	
	public JedisRedisCacheProcessor(String key, String host, int port, String password, ExpiredStrategy expiredStrategy) {
		super(key, CacheConfiguration.wrapRedisOperator(new JedisRedisOperator(host, port, password)), expiredStrategy);
	}
	
	public JedisRedisCacheProcessor(String key, JedisPoolConfig jedisPoolConfig, String host, int port, String password, ExpiredStrategy expiredStrategy) {
		super(key, CacheConfiguration.wrapRedisOperator(new JedisRedisOperator(host, port, password, jedisPoolConfig)), expiredStrategy);
	}
	
}
