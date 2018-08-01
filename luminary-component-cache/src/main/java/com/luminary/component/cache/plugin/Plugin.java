/**  
* <p>Title: Plugin.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年8月1日上午9:58:10  
*/  
package com.luminary.component.cache.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

import com.luminary.component.cache.operator.redis.RedisOperator;

/**  
* <p>Title: Plugin</p>  
* <p>Description: 动态代理处理类</p>  
* @author wulinfeng
* @date 2018年8月1日上午9:58:10
*/
public class Plugin implements InvocationHandler {

	 private final Object target;
	 private final Interceptor interceptor;

	 private Plugin(Object target, Interceptor interceptor) {
	    this.target = target;
	    this.interceptor = interceptor;
	 }
	
	/* (non-Javadoc)  
	 * <p>Title: invoke</p>  
	 * <p>Description: </p>  
	 * @param proxy
	 * @param method
	 * @param args
	 * @return
	 * @throws Throwable  
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])  
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method != null)
	        return interceptor.intercept(new Invocation(target, method, args));
	    return method.invoke(target, args);
	}
	
	/**
	 * 
	 * <p>Title: wrap</p>  
	 * <p>Description: 生成代理对象</p>  
	 * @param target 需要被代理的接口
	 * @param interceptor 拦截器
	 * @return
	 */
	public static Object wrap(Object target, Interceptor interceptor) {
	    Class<?> type = target.getClass();
	    Class<?>[] interfaces = getAllInterfaces(type);
	    if (interfaces.length > 0) {
	      return Proxy.newProxyInstance(
	          type.getClassLoader(),
	          interfaces,
	          new Plugin(target, interceptor));
	    }
	    return target;
	  }
	
	/**
	 * 
	 * <p>Title: getAllInterfaces</p>  
	 * <p>Description: 获得被代理对象的所有可代理接口</p>  
	 * @param type
	 * @return
	 */
	private static Class<?>[] getAllInterfaces(Class<?> type) {
	    Set<Class<?>> interfaces = new HashSet<Class<?>>();
	    while (type != null) {
	      for (Class<?> c : type.getInterfaces()) {
	    	 //  必须实现了RedisOperator接口
	    	 if(c.getName().equals(RedisOperator.class.getName()))
	    		 interfaces.add(c);
	      }
	      type = type.getSuperclass();
	    }
	    return interfaces.toArray(new Class<?>[interfaces.size()]);
	  }

}
