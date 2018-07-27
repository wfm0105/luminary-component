/**  
* <p>Title: TestRule.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月27日下午2:52:02  
*/  
package com.luminary.component.ribbon.rule;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.luminary.component.ribbon.event.ServerEvent;
import com.luminary.component.ribbon.listener.ServerListener;
import com.luminary.component.ribbon.publisher.ServerPublisher;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: TestRule</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年7月27日下午2:52:02
*/
@Slf4j
public class LuminaryRibbonRule extends ZoneAvoidanceRule implements ServerPublisher {

	public static Queue<ServerListener> serverListeners;
	
	public static void regist(ServerListener serverListener) {
		if(serverListeners == null)
			 synchronized (LuminaryRibbonRule.class) {
				 if(serverListeners == null) {
					 serverListeners = new ConcurrentLinkedQueue<ServerListener>();
				 }
			 }
		
		serverListeners.add(serverListener);
	}
	
	/* (non-Javadoc)  
	 * <p>Title: notifyAll</p>  
	 * <p>Description: </p>  
	 * @param serverEvent  
	 * @see com.luminary.component.ribbon.publisher.ServerPublisher#notifyAll(com.luminary.component.ribbon.event.ServerEvent)  
	 */
	@Override
	public void notifyAll(ServerEvent serverEvent) {
         for(ServerListener serverListener : serverListeners) {
        	 serverListener.chooseServer(serverEvent);
         }
	}
	
	@Override
    public Server choose(Object key) {
		Server server = super.choose(key);
        if(server != null) {
        	log.info("select server:"+server.getHostPort());
        }
        ServerEvent serverEvent = new ServerEvent();
        serverEvent.setServer(server);
        notifyAll(serverEvent);
        return server;
	}

}
