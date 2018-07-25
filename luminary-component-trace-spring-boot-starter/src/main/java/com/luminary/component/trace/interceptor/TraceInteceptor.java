/**  
* <p>Title: TraceInteceptor.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日下午3:18:44  
*/  
package com.luminary.component.trace.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.luminary.component.trace.annotation.Trace;
import com.luminary.component.trace.holder.MvcHolder;
import com.luminary.component.trace.tracker.SpringMvcTracker;
import com.luminary.component.trace.tracker.Tracker;

import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: TraceInteceptor</p>  
* <p>Description: 链路跟踪拦截器</p>  
* @author wulinfeng
* @date 2018年7月20日下午3:18:44
*/
@Slf4j
public class TraceInteceptor extends HandlerInterceptorAdapter {

	private Tracker<MvcHolder> tracker;
	
	public TraceInteceptor (Tracker<MvcHolder> tracker) {
		super();
		this.tracker = tracker;
	}
	
	/**
	 * 
	 * http请求处理前操作
	 * 
	 */
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
		log.info("http preHandle");
		if(isTraceHandler(handler)) {
			MvcHolder mvcHolder = new MvcHolder();
			mvcHolder.setRequest(request);
			mvcHolder.setResponse(response);
			mvcHolder.setHandler(handler);
			tracker.preHandle(mvcHolder);
		}
		
		return super.preHandle(request, response, handler);
	}
	
	/**
	 * 
	 * http请求处理完操作
	 * 
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		log.info("http afterCompletion");
		MvcHolder mvcHolder = new MvcHolder();
		mvcHolder.setRequest(request);
		mvcHolder.setResponse(response);
		mvcHolder.setHandler(handler);
		mvcHolder.setException(ex);
		tracker.postHandle(mvcHolder);
	}
	
	/**
	 * 
	 * <p>Title: isTraceHandler</p>  
	 * <p>Description: 是否被@Trace注释</p>  
	 * @param handler
	 * @return
	 */
	public boolean isTraceHandler(Object handler) {
        Object annoation = getAnnotation(handler);
        if (annoation == null)
            return false;
        Trace trace = (Trace) annoation;
        if(!trace.value().getName().equals(SpringMvcTracker.class.getName()))
        	return false;
        return true;
    }
	
	/**
	 * 
	 * <p>Title: getAnnotation</p>  
	 * <p>Description: 获得@Trace注释</p>  
	 * @param handler
	 * @return
	 */
	private Object getAnnotation(Object handler) {
		if(handler == null)
			return null;
		
    	if (!(handler instanceof HandlerMethod))
            return null;
    	
    	Object annoation = null;
    	
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        annoation = handlerMethod.getMethodAnnotation(Trace.class);
        return annoation;
    }
	
}
