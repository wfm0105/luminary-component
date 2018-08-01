/**  
* <p>Title: Invocation.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年8月1日上午9:56:37  
*/  
package com.luminary.component.cache.plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**  
* <p>Title: Invocation</p>  
* <p>Description: 拦截器调用的参数封装</p>  
* @author wulinfeng
* @date 2018年8月1日上午9:56:37
*/
public class Invocation {

	  private final Object target;
	  private final Method method;
	  private final Object[] args;
	
	  public Invocation(Object target, Method method, Object[] args) {
	    this.target = target;
	    this.method = method;
	    this.args = args;
	  }
	
	  public Object getTarget() {
	    return target;
	  }
	
	  public Method getMethod() {
	    return method;
	  }
	
	  public Object[] getArgs() {
	    return args;
	  }
	
	  public Object proceed() throws InvocationTargetException, IllegalAccessException {
	    return method.invoke(target, args);
	  }
	
}
