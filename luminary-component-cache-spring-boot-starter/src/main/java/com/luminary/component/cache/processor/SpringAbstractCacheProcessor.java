/**  
* <p>Title: SpringAbstractCacheProcessor.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午4:50:17  
*/  
package com.luminary.component.cache.processor;

import java.util.function.Function;

import com.luminary.component.cache.expired.ExpiredStrategy;
import com.luminary.component.cache.processor.redis.RedisCacheProcessor;

/**  
* <p>Title: SpringAbstractCacheProcessor</p>  
* <p>Description: 用于扩展的缓存处理类，这里提供缓存处理的模板</p>  
* @author wulinfeng
* @date 2018年7月31日下午4:50:17
*/
public abstract class SpringAbstractCacheProcessor extends AbstractCacheProcessor implements SpringCacheProcessor {

	protected RedisCacheProcessor processor;
	
	public SpringAbstractCacheProcessor(RedisCacheProcessor processor) {
		this.processor = processor;
	}
	
	@Override
	public String getData(String keyPrefix, String key, Function<String, String> operator) {
		return processor.getData(keyPrefix, key, operator);
	}

	@Override
	public String getData(String keyPrefix, String key, Function<String, String> operator, ExpiredStrategy expiredStrategy) {
		return processor.getData(keyPrefix, key, operator, expiredStrategy);
	}
	
	@Override
	public boolean addData(String keyPrefix, String key, Function<String, String> operator) {
		return processor.addData(keyPrefix, key, operator);
	}

	@Override
	public boolean updateData(String keyPrefix, String key, Function<String, Boolean> operator) {
		return processor.updateData(keyPrefix, key, operator);
	}

	@Override
	public boolean deleteData(String key, Function<String, Boolean> operator) {
		return processor.deleteData(key, operator);
	}

	@Override
	public abstract boolean simpleCache(String keyPrefix, String key);

	@Override
	public abstract boolean cache(String key, String data);

	@Override
	public abstract boolean cache(String keyPrefix, String key, String data);

	@Override
	public abstract boolean deleteCache(String key);

	@Override
	public abstract boolean isValid(String keyPrefix, String key);

	@Override
	public abstract boolean simpleCache(String keyPrefix, String key, String data);

	@Override
	public abstract String getDataFromCache(String key);

}
