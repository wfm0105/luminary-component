/**  
* <p>Title: LuminaryRibbonConfig.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月27日下午3:18:10  
*/  
package com.luminary.component.ribbon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.luminary.component.ribbon.rule.LuminaryRibbonRule;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

/**  
* <p>Title: LuminaryRibbonConfig</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年7月27日下午3:18:10
*/
@Configuration
public class LuminaryRibbonConfiguration {

	@Value("${ribbon.client.name:''}")
	private String name = "client";
	
	@Autowired
	private PropertiesFactory propertiesFactory;
	
	@Bean
	public IClientConfig ribbonClientConfig() {
		DefaultClientConfigImpl config = new DefaultClientConfigImpl();
		config.loadProperties(this.name);
		config.set(CommonClientConfigKey.ConnectTimeout, RibbonClientConfiguration.DEFAULT_CONNECT_TIMEOUT);
		config.set(CommonClientConfigKey.ReadTimeout, RibbonClientConfiguration.DEFAULT_READ_TIMEOUT);
		return config;
	}
	
	@Bean
	public IRule ribbonRule(IClientConfig config) {
		if (this.propertiesFactory.isSet(IRule.class, name)) {
			return this.propertiesFactory.get(IRule.class, config, name);
		}
		ZoneAvoidanceRule rule = new LuminaryRibbonRule();
		rule.initWithNiwsConfig(config);
		return rule;
	}
	
}
