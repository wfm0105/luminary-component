/**  
* <p>Title: SpringCacheProcessor.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午4:48:50  
*/  
package com.luminary.component.cache.processor;

import java.util.function.Function;

import com.luminary.component.cache.expired.ExpiredStrategy;

/**  
* <p>Title: SpringCacheProcessor</p>  
* <p>Description: 基于spring的缓存处理接口</p>  
* @author wulinfeng
* @date 2018年7月31日下午4:48:50
*/
public interface SpringCacheProcessor {

	String getData(String keyPrefix, String key, Function<String, String> operator);
	
	String getData(String keyPrefix, String key, Function<String, String> operator, ExpiredStrategy expiredStrategy);
	
	boolean addData(String keyPrefix, String key, Function<String, String> operator);
	
	boolean updateData(String keyPrefix, String key, Function<String, Boolean> operator);
	
	boolean deleteData(String key, Function<String, Boolean> operator);
	
	boolean simpleCache(String keyPrefix, String key);
	
	boolean cache(String key, String data);
	
	boolean cache(String keyPrefix, String key, String data);
	
	boolean cache(String keyPrefix, String key, String data, ExpiredStrategy expiredStrategy);
	
	boolean deleteCache(String key);
	
	boolean isValid(String keyPrefix, String key);
	
}
