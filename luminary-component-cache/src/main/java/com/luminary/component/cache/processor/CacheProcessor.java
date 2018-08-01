/**  
* <p>Title: CacheProcessor.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午4:23:10  
*/  
package com.luminary.component.cache.processor;

import java.util.function.Function;

import com.luminary.component.cache.expired.ExpiredStrategy;

/**  
* <p>Title: CacheProcessor</p>  
* <p>Description: 缓存处理接口</p>  
* @author wulinfeng
* @date 2018年7月31日下午4:23:10
*/
public interface CacheProcessor {

	String getData(Function<String, String> operator);
	
	boolean addData(Function<String, String> operator);
	
	boolean updateData(Function<String, Boolean> operator);
	
	boolean deleteData(Function<String, Boolean> operator);
	
	boolean simpleCache(String keyPrefix, String key, String data);
	
	boolean cache(String key, String data);
	
	boolean cache(String keyPrefix, String key, String data);
	
	boolean cache(String key, String data, ExpiredStrategy expiredStrategy);
	
	boolean cache(String keyPrefix, String key, String data, ExpiredStrategy expiredStrategy);
	
	boolean deleteCache(String key);
	
	boolean isValid(String keyPrefix, String key);
	
	String getKeyPrefix();
	
	void setKeyPrefix(String keyPrefix);
	
	String getKey();
	
	void setKey(String key);
	
}
