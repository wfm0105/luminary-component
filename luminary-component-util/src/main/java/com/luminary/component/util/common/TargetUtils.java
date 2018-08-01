package com.luminary.component.util.common;

import java.lang.reflect.Field;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

/**
 * 
 * 找到代理类的原始类，在使用了aop，比如事务的时候，反射是无法执行代理类的相应方法，需要还原成原始类
 * 
 * @author wulinfeng
 * @date 2017-09-15
 *
 */
public class TargetUtils {

	public static Object getTarget(Object proxy) throws Exception{
		
		if(!AopUtils.isAopProxy(proxy)){
			return proxy;
		}
		
		if(AopUtils.isJdkDynamicProxy(proxy)){
			return getJdkDynamicProxyTargetObject(proxy);
		}
		else{
			return getCglibProxyTargetObject(proxy);
		}
		
	}
	
	private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception{
		
		Field h=proxy.getClass().getSuperclass().getDeclaredField("h");
		h.setAccessible(true);
		AopProxy aopProxy=(AopProxy) h.get(proxy);
		Field advised=aopProxy.getClass().getDeclaredField("advised");
		advised.setAccessible(true);
		Object target=((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
		
		return target;
		
	}
	
	private static Object getCglibProxyTargetObject(Object proxy) throws Exception{
		
		Field h=proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
		h.setAccessible(true);
		Object dynamicAdvisedInterceptor=h.get(proxy);
		Field advised=dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
		advised.setAccessible(true);
		Object target=((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
		
		return target;
		
	}
	
}
