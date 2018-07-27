/**  
* <p>Title: FeignTraceHolder.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月27日下午4:12:40  
*/  
package com.luminary.component.feign.model;

import com.luminary.component.ribbon.event.ServerEvent;
import com.luminary.component.ribbon.listener.ServerListener;
import com.luminary.component.trace.tracker.GenericTracker.TraceHolder;
import com.netflix.loadbalancer.Server;

/**  
* <p>Title: FeignTraceHolder</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年7月27日下午4:12:40
*/
public class FeignTraceHolder extends TraceHolder implements ServerListener {

	/* (non-Javadoc)  
	 * <p>Title: chooseServer</p>  
	 * <p>Description: </p>  
	 * @param serverEvent  
	 * @see com.luminary.component.ribbon.listener.ServerListener#chooseServer(com.luminary.component.ribbon.event.ServerEvent)  
	 */
	@Override
	public void chooseServer(ServerEvent serverEvent) {
		String serverHost = "";
		Server server = serverEvent.getServer();
		if(server != null) {
			serverHost = server.getHostPort();
		}
		this.getEntity().setServiceHost(serverHost);
	}

}
