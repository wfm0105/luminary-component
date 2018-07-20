/**  
* <p>Title: MvcHolder.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日下午5:32:16  
*/  
package com.luminroy.component.trace.holder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Data;

/**  
* <p>Title: MvcHolder</p>  
* <p>Description: mvc相关数据</p>  
* @author wulinfeng
* @date 2018年7月20日下午5:32:16
*/
@Data
public class MvcHolder {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private Object handler;
	private Exception exception;
	
}
