/**  
* <p>Title: SpringMvcTrackerAutoConfigure.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日下午2:28:55  
*/  
package com.luminary.component.trace.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.luminary.component.elasticsearch.JestClientMgr;
import com.luminary.component.trace.client.ElasticsearchTraceClient;
import com.luminary.component.trace.client.TraceClient;
import com.luminary.component.trace.condition.SpringMybatisTrackerCondition;
import com.luminary.component.trace.condition.SpringRedisCacheTrackerCondition;
import com.luminary.component.trace.context.ProfileContext;
import com.luminary.component.trace.context.RedisContext;
import com.luminary.component.trace.context.TraceClientContext;
import com.luminary.component.trace.holder.MvcHolder;
import com.luminary.component.trace.interceptor.TraceInteceptor;
import com.luminary.component.trace.properties.TraceProperties;
import com.luminary.component.trace.tracker.GenericTracker.TraceHolder;
import com.luminary.component.trace.tracker.SpringMvcTracker;
import com.luminary.component.trace.tracker.SpringMybatisTracker;
import com.luminary.component.trace.tracker.SpringRedisCacheTracker;
import com.luminary.component.trace.tracker.Tracker;

import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: SpringMvcTrackerAutoConfigure</p>  
* <p>Description: 链路跟踪自动配置</p>  
* @author wulinfeng
* @date 2018年7月20日下午2:28:55
*/
@Slf4j
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(TraceProperties.class)
public class TrackerAutoConfiguration implements WebMvcConfigurer {

	 @Value("${spring.profiles.active:default}")
	 private String profile;
	
	 @Autowired
	 private TraceProperties traceProperties;
	 
	 @Autowired
	 private RedisProperties redisProperties;
	 
	 @Autowired
	 private JestClientMgr jestClientMgr;
	 
	 @Bean
	 @ConditionalOnMissingBean
	 public TraceClient traceClient() throws IOException {
		 TraceConfig traceConfig = new TraceConfig();
		 traceConfig.setEsIndex(traceProperties.getEsIndex());
		 traceConfig.setEsType(traceProperties.getEsType());
		 TraceClient traceClient = new ElasticsearchTraceClient(traceConfig, jestClientMgr.getJestClient());
		 return traceClient;
	 }
	 
	 @Bean
	 public Tracker<MvcHolder> springMvcTracker() throws IOException {
		 TraceClient traceClient = traceClient();
		 Tracker<MvcHolder> tracker = new SpringMvcTracker(traceClient);
		 return tracker;
	 }
	 
	 @Bean
	 @Conditional(SpringMybatisTrackerCondition.class)
	 public Tracker<TraceHolder> springMybatisTracker() throws IOException {
		 TraceClient traceClient = traceClient();
		 Tracker<TraceHolder> tracker = new SpringMybatisTracker(traceClient);
		 return tracker;
	 }
	 
	 @Bean
	 @Conditional(SpringRedisCacheTrackerCondition.class)
	 public Tracker<TraceHolder> springRedisCacheTracker() throws IOException {
		 TraceClient traceClient = traceClient();
		 Tracker<TraceHolder> tracker = new SpringRedisCacheTracker(traceClient, profile, redisProperties.getHost() + ":" +redisProperties.getPort());
		 return tracker;
	 }
	 
	 @Override
     public void addInterceptors(InterceptorRegistry registry) {
		TraceInteceptor traceInteceptor;
		try {
			traceInteceptor = new TraceInteceptor(profile, springMvcTracker());
			registry.addInterceptor(traceInteceptor).addPathPatterns(traceProperties.getPathPatterns());
		} catch (IOException e) {
			log.error("trace拦截器设置失败！", e);
		}
	 }
	 
	 @Bean
	 public ProfileContext profileContext() throws IOException {
		 ProfileContext profileHelper = new ProfileContext();
		 profileHelper.setProfile(profile);
		 return profileHelper;
	 }
	 
	 @Bean
	 public TraceClientContext traceClientContext() throws IOException {
		 TraceClientContext traceClientContext = new TraceClientContext();
		 traceClientContext.setTraceClient(traceClient());
		 return traceClientContext;
	 }
	 
	 @Bean
	 @Conditional(SpringRedisCacheTrackerCondition.class)
	 public RedisContext redisContext() throws IOException {
		 RedisContext redisContext = new RedisContext();
		 redisContext.setHost(redisProperties.getHost());
		 redisContext.setPort(redisProperties.getPort());
		 return redisContext;
	 }
	
}
