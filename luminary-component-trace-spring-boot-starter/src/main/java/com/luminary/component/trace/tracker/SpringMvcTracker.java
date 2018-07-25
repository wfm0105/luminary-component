/**  
* <p>Title: SpringMvcTracker.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日上午10:23:31  
*/  
package com.luminary.component.trace.tracker;

import com.luminary.component.trace.client.TraceClient;
import com.luminary.component.trace.holder.MvcHolder;

/**  
* <p>Title: SpringMvcTracker</p>  
* <p>Description: 适用于spring mvc的tracker</p>  
* @author wulinfeng
* @date 2018年7月20日上午10:23:31
*/
public class SpringMvcTracker extends MvcTracker implements Tracker<MvcHolder> {

	public SpringMvcTracker (TraceClient traceClient) {
		super(traceClient);
		this.traceClient = traceClient;
	}
	
}
