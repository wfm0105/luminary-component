/**  
* <p>Title: JedisRedisOperate.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午4:01:34  
*/  
package com.luminary.component.cache.operator.redis;

import com.luminary.component.util.common.JedisUtil;

import redis.clients.jedis.JedisPoolConfig;

/**  
* <p>Title: JedisRedisOperate</p>  
* <p>Description: 基于jedis的redis操作</p>  
* @author wulinfeng
* @date 2018年7月31日下午4:01:34
*/
public class JedisRedisOperator implements RedisOperator {
	
	private JedisUtil jedisUtil;
	
	public JedisRedisOperator(String host, int port, String password) {
		this.jedisUtil = new JedisUtil(host, port, password);
	}
	
	public JedisRedisOperator(String host, int port, String password, JedisPoolConfig jedisPoolConfig) {
		this.jedisUtil = new JedisUtil(host, port, password, jedisPoolConfig);
	}
	
	@Override
	public boolean setBit(String key, int offset, boolean value) {
		return jedisUtil.setBit(key, offset, value);
	}

	@Override
	public boolean getBit(String key, int offset) {
		return jedisUtil.getBit(key, offset);
	}

	@Override
	public String set(String key, String data, int expiredSeconds) {
		return jedisUtil.set(key, data, expiredSeconds);
	}

	@Override
	public String get(String key) {
		return jedisUtil.get(key);
	}
	
	@Override
	public long del(String key) {
		return jedisUtil.del(key);
	}

}
