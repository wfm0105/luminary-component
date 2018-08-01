/**  
* <p>Title: RedisTemplateRedisOperator.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午4:41:54  
*/  
package com.luminary.component.cache.operator.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**  
* <p>Title: RedisTemplateRedisOperator</p>  
* <p>Description: 基于redisTemplate的redis操作</p>  
* @author wulinfeng
* @date 2018年7月31日下午4:41:54
*/
public class RedisTemplateRedisOperator implements RedisOperator {

	private RedisTemplate<String, String> redisTemplate;
	
	public RedisTemplateRedisOperator(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@Override
	public boolean setBit(String key, int offset, boolean value) {
		return redisTemplate.opsForValue().setBit(key, offset, value);
	}

	@Override
	public boolean getBit(String key, int offset) {
		return redisTemplate.opsForValue().getBit(key, offset);
	}

	@Override
	public String set(String key, String data, int expiredSeconds) {
		redisTemplate.opsForValue().set(key, data, expiredSeconds, TimeUnit.SECONDS);
		return null;
	}

	@Override
	public String get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	@Override
	public long del(String key) {
		redisTemplate.delete(key);
		return 0;
	}
	
	public String getHostPort() {
		return redisTemplate.getConnectionFactory().getConnection().getClientName();
	}

}
