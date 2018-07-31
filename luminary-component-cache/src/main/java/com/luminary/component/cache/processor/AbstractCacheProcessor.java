/**  
* <p>Title: AbstractCacheProcessor.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午4:24:40  
*/  
package com.luminary.component.cache.processor;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luminary.component.cache.expired.ExpiredStrategy;

/**  
* <p>Title: AbstractCacheProcessor</p>  
* <p>Description: 用于扩展的缓存处理类，这里提供缓存处理的模板</p>  
* @author wulinfeng
* @date 2018年7月31日下午4:24:40
*/
public abstract class AbstractCacheProcessor implements CacheProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractCacheProcessor.class);
	
	protected String key;
	
	protected String keyPrefix;
	
	protected boolean disableAll = false;
	
	protected String[] disableKeys = {};
	
	protected AbstractCacheProcessor() {
		
	}
	
	protected AbstractCacheProcessor(String key) {
		this.key = key;
	}
	
	public String getData(String keyPrefix, String key, Function<String, String> operator) {
		return getData(keyPrefix, key, operator, null);
	}

	public String getData(String keyPrefix, String key, Function<String, String> operator, ExpiredStrategy expiredStrategy) {
		
		logger.info("disableAll="+disableAll);
		
		// 禁用所有缓存
		if(disableAll) {
			logger.warn("禁用所有缓存！");
			return getDataFromDB(keyPrefix, key, operator, expiredStrategy);
		}
		
		// 禁用部分缓存
		List<String> disableKeyList = Arrays.asList(disableKeys);
		if(!disableKeyList.isEmpty()) {
			boolean isMatch = 
					disableKeyList.stream()
							  .anyMatch(k->{
								 return k.equals(keyPrefix);
							  });
			if(isMatch) {
				logger.warn("键"+keyPrefix+"禁用缓存！");
				return getDataFromDB(keyPrefix, key, operator, expiredStrategy);
			}
		}
	
		// 没有禁用缓存
		if(!isValid(keyPrefix, key)) {
			return "";
		} else {
			// 可能存在，或者是hash冲突
			String data = getDataFromCache(key);
			// 缓存存在
			if(data != null) {
				return data;
			} else {
				return getDataFromDB(keyPrefix, key, operator, expiredStrategy);
			}
		}
		
	}
	
	protected String getDataFromDB(String keyPrefix, String key, Function<String, String> operator, ExpiredStrategy expiredStrategy) {
		String data = operator.apply(key);
		if(data == null) {
			data = "";
		}
		
		if(expiredStrategy == null)
			cache(keyPrefix, key, data);
		else
			cache(keyPrefix, key, data, expiredStrategy);
		return data;
	}
	
	@Override
	public String getData(Function<String, String> operator) {
		return getData(keyPrefix, key, operator);
	}
	
	public boolean addData(String keyPrefix, String key, Function<String, String> operator) {
		return changeOperateData(keyPrefix, key, operator);
	}
	
	@Override
	public boolean addData(Function<String, String> operator) {
		return changeOperateData(operator);
	}
	
	public boolean updateData(String keyPrefix, String key, Function<String, Boolean> operator) {
		return deleteOperateData(key, operator);
	}
	
	@Override
	public boolean updateData(Function<String, Boolean> operator) {
		return deleteOperateData(operator);
	}
	
	public boolean deleteData(String key, Function<String, Boolean> operator) {
		return deleteOperateData(key, operator);
	}
	
	@Override
	public boolean deleteData(Function<String, Boolean> operator) {
		return deleteOperateData(operator);
	}

	@Override
	public abstract boolean simpleCache(String keyPrefix, String key, String data); 
	
	@Override
	public abstract boolean cache(String key, String data);
	
	@Override
	public abstract boolean cache(String keyPrefix, String key, String data);
	
	@Override
	public abstract boolean cache(String key, String data, ExpiredStrategy expiredStrategy);
	
	@Override
	public abstract boolean cache(String keyPrefix, String key, String data, ExpiredStrategy expiredStrategy);
	
	@Override
	public abstract boolean isValid(String keyPrefix, String key);
	
	@Override
	public abstract boolean deleteCache(String key);
	
	public abstract String getDataFromCache(String key);
	
	private boolean changeOperateData(String keyPrefix, String key, Function<String, String> operator) {
		String data = operator.apply(key) ;
		return simpleCache(keyPrefix, key, data);
	}
	
	private boolean changeOperateData(Function<String, String> operator) {
		String data = operator.apply(key) ;
		return simpleCache(keyPrefix, key, data);
	}
	
	private boolean deleteOperateData(String key, Function<String, Boolean> operator) {
		if(operator.apply(key)) {
			return deleteCache(key);
		}
		return false;
	}
	
	private boolean deleteOperateData(Function<String, Boolean> operator) {
		if(operator.apply(key)) {
			return deleteCache(key);
		}
		return false;
	}

	@Override
	public String getKeyPrefix() {
		return keyPrefix;
	}

	@Override
	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
	}
	
}
