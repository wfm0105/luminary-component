/**  
* <p>Title: LuminroyRequestInterceptor.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月23日下午2:27:59  
*/  
package com.luminroy.component.feign.interceptor;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.luminroy.component.trace.model.TraceInfo;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: LuminroyRequestInterceptor</p>  
* <p>Description: 拦截器，转发http头中的token以及处理链路跟踪信息</p>  
* @author wulinfeng
* @date 2018年7月23日下午2:27:59
*/
@Slf4j
public class LuminroyRequestInterceptor implements RequestInterceptor {

	public static String TOKEN_HEADER = "authorization";
	
	/* (non-Javadoc)  
	 * <p>Title: apply</p>  
	 * <p>Description: </p>  
	 * @param arg0  
	 * @see feign.RequestInterceptor#apply(feign.RequestTemplate)  
	 */
	@Override
	public void apply(RequestTemplate template) {
		HttpServletRequest request = getHttpServletRequest();
		if(request == null) {
			log.warn("request is null in feign request interceptor");
			return;
		}
		Map<String, String> headerMap = getHeaders(request);
		if(headerMap.get(TOKEN_HEADER) != null) {
			template.header(TOKEN_HEADER, headerMap.get(TOKEN_HEADER));
		}
		if(request.getAttribute(TraceInfo.TRACE_ID_KEY) != null) {
			template.header(TraceInfo.TRACE_ID_KEY, (String) request.getAttribute(TraceInfo.TRACE_ID_KEY));
		}
		if(request.getAttribute(TraceInfo.RPC_ID_KEY) != null) {
			template.header(TraceInfo.RPC_ID_KEY, (String) request.getAttribute(TraceInfo.RPC_ID_KEY));
		}
	}
	
	private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }
	
	private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

}
