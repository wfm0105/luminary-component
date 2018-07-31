/**  
* <p>Title: RedisTemplateCacheProcessor.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午4:56:57  
*/  
package com.luminary.component.cache.processor.redis;

import com.luminary.component.cache.expired.ExpiredStrategy;
import com.luminary.component.cache.operator.redis.RedisOperator;

/**  
* <p>Title: RedisTemplateCacheProcessor</p>  
* <p>Description: 基于redisTemplate的redis缓存处理类</p>  
* @author wulinfeng
* @date 2018年7月31日下午4:56:57
*/
public class RedisTemplateCacheProcessor extends RedisCacheProcessor {

	public RedisTemplateCacheProcessor(RedisOperator redisOperate) {
		super(redisOperate);
	}
	
	public RedisTemplateCacheProcessor(RedisOperator redisOperate, ExpiredStrategy expiredStrategy) {
		super(redisOperate, expiredStrategy);
	}
	
}
