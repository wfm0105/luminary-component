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
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年7月27日下午3:44:40
*/
public interface ServerListener {

	void chooseServer(ServerEvent serverEvent);
	
}
