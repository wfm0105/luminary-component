/**  
* <p>Title: TraceDemo.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日下午2:59:54  
*/  
package com.luminary.component.trace.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.luminary.component.feign.tracker.FeignTracker;
import com.luminary.component.hystrix.tracker.HystrixTracker;
import com.luminary.component.ribbon.client.LuminaryRibbonClient;
import com.luminary.component.trace.annotation.Trace;
import com.luminary.component.trace.client.TraceClient;
import com.luminary.component.trace.demo.service.Service;
import com.luminary.component.trace.demo.service.TestService;
import com.luminary.component.trace.tracker.SpringMvcTracker;

/**  
* <p>Title: TraceDemo</p>  
* <p>Description: 链路跟踪demo</p>  
* @author wulinfeng
* @date 2018年7月20日下午2:59:54
*/
@Import(value= {FeignTracker.class, HystrixTracker.class, LuminaryRibbonClient.class})
@RestController
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class TraceDemoApplication {

	@Autowired
	private TraceClient traceClient;
	
	@Autowired
	private Service service;
	
	@Autowired
	private TestService testService;
	
	public static void main(String[] args) {
        SpringApplication.run(TraceDemoApplication.class, args);
    }
	
	@Trace(SpringMvcTracker.class)
	@GetMapping("/server3")
	public String server3(
			HttpServletRequest request,
			HttpServletResponse response) {
		
		testService.get(888);
		return "hello world3！";
		
	}
	
	@Trace(SpringMvcTracker.class)
	@GetMapping("/server4")
	public String server4(
			HttpServletRequest request,
			HttpServletResponse response) {
		
		String exception = null;
		exception.getBytes();
		return "hello world3！";
		
	}
	
	@Trace(SpringMvcTracker.class)
	@GetMapping("/feignServer1")
	public String feignServer1(
			HttpServletRequest request,
			HttpServletResponse response) {
		
		service.feignServer2();
		service.feignServer3();
		return "hello world！";
		
	}
	
	@Trace(SpringMvcTracker.class)
	@GetMapping("/feignServer2")
	public String feignServer2(
			HttpServletRequest request,
			HttpServletResponse response) {
		
		service.feignServer3();
		return "hello world！";
		
	}
	
	@Trace(SpringMvcTracker.class)
	@GetMapping("/feignMybatisServer1/{number}")
	public String feignMybatisServer1(
			@PathVariable("number") long number,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		testService.get(number);
		service.feignServer2();
		service.feignServer3();
		return "hello world！";
		
	}
	
}
