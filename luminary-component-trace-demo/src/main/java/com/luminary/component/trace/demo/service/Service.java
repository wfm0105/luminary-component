/**  
* <p>Title: Service.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月23日下午2:57:17  
*/  
package com.luminary.component.trace.demo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.luminary.component.feign.interceptor.LuminaryRequestInterceptor;
import com.luminary.component.feign.tracker.FeignTracker;
import com.luminary.component.trace.annotation.Trace;

/**  
* <p>Title: Service</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年7月23日下午2:57:17
*/
@FeignClient(name="trace-demo2-server", configuration = LuminaryRequestInterceptor.class)
public interface Service {
	
	@Trace(FeignTracker.class)
	@GetMapping("/feignServer2")
	String feignServer2();
	
}
