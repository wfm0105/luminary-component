/**  
* <p>Title: ServerEvent.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月27日下午3:35:32  
*/  
package com.luminary.component.ribbon.event;

import com.netflix.loadbalancer.Server;

import lombok.Data;

/**  
* <p>Title: ServerEvent</p>  
* <p>Description: 负载均衡时，服务相关事件</p>  
* @author wulinfeng
* @date 2018年7月27日下午3:35:32
*/
@Data
public class ServerEvent {

	private Server server;
	
}
