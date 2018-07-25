/**  
* <p>Title: TraceConfig.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日下午1:57:31  
*/  
package com.luminary.component.trace.config;

import lombok.Getter;
import lombok.Setter;

/**  
* <p>Title: TraceConfig</p>  
* <p>Description: traceClient的配置项</p>  
* @author wulinfeng
* @date 2018年7月20日下午1:57:31
*/
public class TraceConfig {

	public static final String DEFAULT_ES_INDEX = "luminroy-trace-${date}";
			
	public static final String DEFAULT_ES_TYPE = "default-trace";
	
	@Setter
	@Getter
	private String esIndex = DEFAULT_ES_INDEX; // elasticsearch索引
	
	@Setter
	@Getter
	private String esType = DEFAULT_ES_TYPE; // elasticsearch类型
	
}
