/**  
* <p>Title: SpringRedisCacheTracker.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年8月1日下午2:03:53  
*/  
package com.luminary.component.trace.tracker;

import com.luminary.component.trace.client.TraceClient;
import com.luminary.component.trace.context.ProfileContext;
import com.luminary.component.trace.context.RedisContext;
import com.luminary.component.trace.context.TraceClientContext;

/**  
* <p>Title: SpringRedisCacheTracker</p>  
* <p>Description: 基于spring的redis缓存链路跟踪器</p>  
* @author wulinfeng
* @date 2018年8月1日下午2:03:53
*/
public class SpringRedisCacheTracker extends RedisCacheTracker {

	private String profile;
	
	private String hostPort;
	
	public SpringRedisCacheTracker() {
		super();
	}
	
	public SpringRedisCacheTracker(TraceClient traceClient, String profile, String hostPort) {
		super(traceClient);
		this.profile = profile;
		this.hostPort = hostPort;
	}
	
	@Override
	public String getProfile() {
		if(profile == null)
			return ProfileContext.profile;
		return profile;
	}

	@Override
	public TraceClient getTraceClient() {
		if(traceClient == null)
			return TraceClientContext.traceClient;
		return traceClient;
	}
	
	@Override
	public String getServerHost() {
		if(hostPort == null)
			return RedisContext.host+":"+RedisContext.port;
		return hostPort;
	}
	
}
