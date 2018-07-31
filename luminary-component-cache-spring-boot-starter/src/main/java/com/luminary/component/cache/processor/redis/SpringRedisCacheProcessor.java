/**  
* <p>Title: SpringRedisCacheProcessor.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午4:55:14  
*/  
package com.luminary.component.cache.processor.redis;

import com.luminary.component.cache.expired.ExpiredStrategy;
import com.luminary.component.cache.processor.SpringAbstractCacheProcessor;

/**  
* <p>Title: SpringRedisCacheProcessor</p>  
* <p>Description: 基于spring的redis缓存处理类</p>  
* @author wulinfeng
* @date 2018年7月31日下午4:55:14
*/
public class SpringRedisCacheProcessor extends SpringAbstractCacheProcessor{

	public SpringRedisCacheProcessor(RedisCacheProcessor processor) {
		super(processor);
	}
	
	@Override
	public boolean simpleCache(String keyPrefix, String key) {
		return processor.simpleCache(keyPrefix, key, "");
	}

	@Override
	public boolean cache(String key, String data) {
		return processor.cache(key, data);
	}

	@Override
	public boolean cache(String keyPrefix, String key, String data) {
		return processor.cache(keyPrefix, key, data);
	}
	
	@Override
	public boolean cache(String key, String data, ExpiredStrategy expiredStrategy) {
		return processor.cache(key, data, expiredStrategy);
	}

	@Override
	public boolean cache(String keyPrefix, String key, String data, ExpiredStrategy expiredStrategy) {
		return processor.cache(keyPrefix, key, data, expiredStrategy);
	}

	@Override
	public boolean deleteCache(String key) {
		return processor.deleteCache(key);
	}

	@Override
	public boolean isValid(String keyPrefix, String key) {
		return processor.isValid(keyPrefix, key);
	}

	@Override
	public boolean simpleCache(String keyPrefix, String key, String data) {
		return processor.simpleCache(keyPrefix, key, data);
	}

	@Override
	public String getDataFromCache(String key) {
		return processor.getDataFromCache(key);
	}

}

