/**  
* <p>Title: ServerPublisher.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月27日下午3:48:18  
*/  
package com.luminary.component.ribbon.publisher;

import com.luminary.component.ribbon.event.ServerEvent;
import com.luminary.component.ribbon.listener.ServerListener;

/**  
* <p>Title: ServerPublisher</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年7月27日下午3:48:18
*/
public interface ServerPublisher {

	void notifyAll(ServerEvent serverEvent);
	
}
