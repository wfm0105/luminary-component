package com.luminary.component.util.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie操作工具类
 * 
 * @author huangtf
 * @date 2016/12/15
 *
 */
public final class CookieUtil {
	

	/**
	 * 获取cookie
	 * 
	 * @param request
	 * @param cookieName cookie名称
	 * @return
	 */
	public static String getCookie(HttpServletRequest request,String cookieName){
	    String cookieValue=null; 
	    
		Cookie[] cookies=request.getCookies();
		if(cookies!=null&&cookies.length>0){
			for(Cookie cookie:cookies){
				if(cookieName.equals(cookie.getName())){//微信浏览器的cookie
					cookieValue=cookie.getValue();
					break;
				}
			}
		}
		
		return cookieValue;
	}
	
	/**
	 * 删除cookie
	 * 
	 * @param cookieName cookie名称
	 */
	public static void deleteCookie(HttpServletResponse response,String cookieName){
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setPath("/");
		cookie.setMaxAge(0); 
		response.addCookie(cookie);  
	}
	
	/**
	 * 添加cookie
	 * 
	 * @param response
	 * @param cookieName cookie名称
	 * @param cookieValue cookie的值
	 * @param expire cookie过期时间(单位：秒)
	 */
	public static void addCookie(HttpServletResponse response,String cookieName,String cookieValue,int expire){
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setPath("/");
		cookie.setMaxAge(expire); 
		response.addCookie(cookie);  
	}

}
