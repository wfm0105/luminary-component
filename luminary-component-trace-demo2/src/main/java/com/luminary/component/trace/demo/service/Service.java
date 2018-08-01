/**  
* <p>Title: Service.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月26日上午11:10:28  
*/  
package com.luminary.component.trace.demo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.luminary.component.feign.interceptor.LuminaryRequestInterceptor;
import com.luminary.component.feign.tracker.FeignTracker;
import com.luminary.component.trace.annotation.Trace;
import com.luminary.component.trace.demo.service.impl.ServiceImpl;

/**  
* <p>Title: Service</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年7月26日上午11:10:28
*/
@FeignClient(name="trace-demo-server", fallback=ServiceImpl.class, configuration = LuminaryRequestInterceptor.class)
public interface Service {

	@Trace(FeignTracker.class)
	@GetMapping("/server3")
	String server3();
	
	@Trace(FeignTracker.class)
	@GetMapping("/server4")
	String server4();
	
}
