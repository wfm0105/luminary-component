/**  
* <p>Title: CacheAutoConfiguration.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午4:59:25  
*/  
package com.luminary.component.cache.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.luminary.component.cache.expired.BaseExpiredStrategy;
import com.luminary.component.cache.expired.ExpiredStrategy;
import com.luminary.component.cache.operator.redis.RedisTemplateRedisOperator;
import com.luminary.component.cache.processor.SpringCacheProcessor;
import com.luminary.component.cache.processor.redis.RedisCacheProcessor;
import com.luminary.component.cache.processor.redis.SpringRedisCacheProcessor;
import com.luminary.component.cache.properties.CacheProperties;

/**  
* <p>Title: CacheAutoConfiguration</p>  
* <p>Description: 缓存处理自动配置</p>  
* @author wulinfeng
* @date 2018年7月31日下午4:59:25
*/
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(CacheAutoConfiguration.class);
	
	@Autowired
	private CacheProperties cacheProperties;
	
	@Autowired
	private RedisTemplateRedisOperator redisTemplateRedisOperator;
	
	@Bean
	@ConditionalOnMissingBean(ExpiredStrategy.class)
	public ExpiredStrategy expiredStrategy() {
		return new BaseExpiredStrategy(cacheProperties.getMinExpiredSeconds(), cacheProperties.getMaxExpiredSeconds());
	}
	
	@Bean
	@ConditionalOnMissingBean(SpringCacheProcessor.class)
	public SpringRedisCacheProcessor cacheProcessor() {
		logger.info("disableAll="+cacheProperties.isDisableAll());
		for(String key : cacheProperties.getDisableKeys()) {
			logger.info("disableKey="+key);
		}
		RedisCacheProcessor redisCacheProcessor = new RedisCacheProcessor(redisTemplateRedisOperator, expiredStrategy(), cacheProperties.isDisableAll(), cacheProperties.getDisableKeys());
		SpringRedisCacheProcessor cacheProcessor = new SpringRedisCacheProcessor(redisCacheProcessor);
		return cacheProcessor;
	}
	
}
