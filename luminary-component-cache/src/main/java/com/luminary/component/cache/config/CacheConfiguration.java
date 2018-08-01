/**  
* <p>Title: CacheConfiguration.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年8月1日上午10:37:25  
*/  
package com.luminary.component.cache.config;

import com.luminary.component.cache.operator.redis.RedisOperator;
import com.luminary.component.cache.plugin.Interceptor;
import com.luminary.component.cache.plugin.Plugin;

/**  
* <p>Title: CacheConfiguration</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年8月1日上午10:37:25
*/
public class CacheConfiguration {

	public static Interceptor interceptor;

	/**
	 * 
	 * <p>Title: wrapRedisOperator</p>  
	 * <p>Description: 获得RedisOperator的代理类</p>  
	 * @param redisOperator
	 * @return
	 */
	public static RedisOperator wrapRedisOperator(RedisOperator redisOperator) {
		if(interceptor == null)
			return redisOperator;
		return (RedisOperator) Plugin.wrap(redisOperator, interceptor);
	}
	
}
