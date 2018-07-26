/**  
* <p>Title: LuminaryHystrixAutoConfigure.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月26日上午10:15:33  
*/  
package com.luminary.component.hystrix.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.luminary.component.hystrix.strategy.RequestAttributeHystrixConcurrencyStrategy;

/**  
* <p>Title: LuminaryHystrixAutoConfigure</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年7月26日上午10:15:33
*/
@Configuration
@ConditionalOnClass(RequestAttributeHystrixConcurrencyStrategy.class)
public class LuminaryHystrixAutoConfigure {

	@Bean
	@ConditionalOnMissingBean
	public RequestAttributeHystrixConcurrencyStrategy requestAttributeHystrixConcurrencyStrategy() {
		return new RequestAttributeHystrixConcurrencyStrategy();
	}
	
}
