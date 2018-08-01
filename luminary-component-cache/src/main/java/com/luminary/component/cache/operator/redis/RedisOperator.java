/**  
* <p>Title: RedisOperator.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午4:01:48  
*/  
package com.luminary.component.cache.operator.redis;

/**  
* <p>Title: RedisOperator</p>  
* <p>Description: 支持的redis操作</p>  
* @author wulinfeng
* @date 2018年7月31日下午4:01:48
*/
public interface RedisOperator {

	boolean setBit(String key, int offset, boolean value);
	
	boolean getBit(String key, int offset);
	
	String set(String key, String data, int expiredSeconds);
	
	String get(String key);
	
	long del(String key);
	
}

