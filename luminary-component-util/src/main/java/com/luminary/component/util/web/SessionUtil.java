package com.luminary.component.util.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Session操作工具类
 * 
 * @author huangtf
 * @date 2017/1/2
 *
 */
public final class SessionUtil {
	
	private static final Logger log = LoggerFactory.getLogger(SessionUtil.class);
	

	/**
	 * 从session中获取信息
	 * 
	 * @param request http请求对象
	 * @param key 属性值对应的key
	 * @return
	 */
	public static Object get(HttpServletRequest request,String key){
	    //String value=null; 
	    
	    HttpSession session = request.getSession();
	    Object value = session.getAttribute(key);
	    log.info("SessionUtil.get(), key="+key+",value="+value);
	    
		return value;
	}
	
	/**
	 * 从session中删除信息
	 * 
	 * @param request http请求对象
	 * @param key 属性值对应的key
	 * @return
	 */
	public static void delete(HttpServletRequest request,String key){
		 HttpSession session = request.getSession();
		 session.removeAttribute(key);
	}
	
	/**
	 * 添加信息到session
	 * 
	 * @param request http请求对象
	 * @param key 属性值对应的key
	 * @param value 属性值
	 * 
	 * @return
	 */
	public static void add(HttpServletRequest request,String key,Object value){
		 HttpSession session = request.getSession();
		 session.setAttribute(key, value);
		 log.info("SessionUtil.add(), key="+key+",value="+value);
	}

}
