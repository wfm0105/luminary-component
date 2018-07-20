/**  
* <p>Title: TraceProperties.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日下午1:13:23  
*/  
package com.luminroy.component.trace.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.luminroy.component.trace.config.TraceConfig;

import lombok.Data;

/**  
* <p>Title: TraceProperties</p>  
* <p>Description: trace组件的配置文件</p>  
* @author wulinfeng
* @date 2018年7月20日下午1:13:23
*/
@Data
@ConfigurationProperties("luminroy.trace")
public class TraceProperties {
	
	private String esIndex = TraceConfig.DEFAULT_ES_INDEX;
	
	private String esType = TraceConfig.DEFAULT_ES_TYPE;
	
	private String pathPatterns = "/**";
	
}
