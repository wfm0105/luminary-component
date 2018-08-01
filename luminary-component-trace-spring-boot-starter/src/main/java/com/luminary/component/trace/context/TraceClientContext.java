/**  
* <p>Title: TraceClientContext.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午2:37:18  
*/  
package com.luminary.component.trace.context;

import com.luminary.component.trace.client.TraceClient;

/**  
* <p>Title: TraceClientContext</p>  
* <p>Description: 非spring管理的bean中用来获取TraceClientContext</p>  
* @author wulinfeng
* @date 2018年7月31日下午2:37:18
*/
public class TraceClientContext {

	public static TraceClient traceClient;
	
	public void setTraceClient(TraceClient traceClient) {
		TraceClientContext.traceClient = traceClient;
	}
	
}
