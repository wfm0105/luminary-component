/**  
* <p>Title: ServerListener.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月27日下午3:44:40  
*/  
package com.luminary.component.ribbon.listener;

import com.luminary.component.ribbon.event.ServerEvent;

/**  
* <p>Title: ServerListener</p>  
* <p>Description: 负载均衡时，服务事件监听者</p>  
* @author wulinfeng
* @date 2018年7月27日下午3:44:40
*/
public interface ServerListener {

	/**
	 * 
	 * <p>Title: chooseServer</p>  
	 * <p>Description: 选择服务事件监听逻辑</p>  
	 * @param serverEvent
	 */
	void chooseServer(ServerEvent serverEvent);
	
}
