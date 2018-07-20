/**  
* <p>Title: SpringMvcTrackerAutoConfigure.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日下午2:28:55  
*/  
package com.luminroy.component.trace.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.luminory.component.elasticsearch.JestClientMgr;
import com.luminroy.component.trace.client.ElasticsearchTranceClient;
import com.luminroy.component.trace.client.TraceClient;
import com.luminroy.component.trace.holder.MvcHolder;
import com.luminroy.component.trace.interceptor.TraceInteceptor;
import com.luminroy.component.trace.properties.TraceProperties;
import com.luminroy.component.trace.tracker.SpringMvcTracker;
import com.luminroy.component.trace.tracker.Tracker;

import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: SpringMvcTrackerAutoConfigure</p>  
* <p>Description: 基于spring mvc的链路跟踪自动配置</p>  
* @author wulinfeng
* @date 2018年7月20日下午2:28:55
*/
@Slf4j
@Configuration
@EnableConfigurationProperties(TraceProperties.class)
public class SpringMvcTrackerAutoConfigure implements WebMvcConfigurer {

	 @Autowired
	 private TraceProperties traceProperties;
	 
	 @Autowired
	 private JestClientMgr jestClientMgr;
	 
	 @Bean
	 @ConditionalOnMissingBean
	 public TraceClient traceClient() throws IOException {
		 TraceConfig traceConfig = new TraceConfig();
		 traceConfig.setEsIndex(traceProperties.getEsIndex());
		 traceConfig.setEsType(traceProperties.getEsType());
		 TraceClient traceClient = new ElasticsearchTranceClient(traceConfig, jestClientMgr.getJestClient());
		 return traceClient;
	 }
	 
	 @Bean
	 public Tracker<MvcHolder> springMvcTracker() throws IOException {
		 TraceClient traceClient = traceClient();
		 Tracker<MvcHolder> tracker = new SpringMvcTracker(traceClient);
		 return tracker;
	 }
	 
	 @Override
     public void addInterceptors(InterceptorRegistry registry) {
		TraceInteceptor traceInteceptor;
		try {
			traceInteceptor = new TraceInteceptor(springMvcTracker());
			registry.addInterceptor(traceInteceptor).addPathPatterns(traceProperties.getPathPatterns());
		} catch (IOException e) {
			log.error("trace拦截器设置失败！", e);
		}
	 }
	
}
