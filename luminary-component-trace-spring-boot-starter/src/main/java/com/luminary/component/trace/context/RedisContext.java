/**  
* <p>Title: RedisContext.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年8月1日下午2:40:53  
*/  
package com.luminary.component.trace.context;

/**  
* <p>Title: RedisContext</p>  
* <p>Description: 非spring管理的bean中用来获取redis配置信息</p>  
* @author wulinfeng
* @date 2018年8月1日下午2:40:53
*/
public class RedisContext {

	public static String host;
	public static int port;
	
	public void setHost(String host) {
		RedisContext.host = host;
	}
	
	public void setPort(int port) {
		RedisContext.port = port;
	}
	
}
