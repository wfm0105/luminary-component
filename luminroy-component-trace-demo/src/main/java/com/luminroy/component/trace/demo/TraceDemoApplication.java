/**  
* <p>Title: TraceDemo.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日下午2:59:54  
*/  
package com.luminroy.component.trace.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luminroy.component.trace.annotation.Trace;

/**  
* <p>Title: TraceDemo</p>  
* <p>Description: 链路跟踪demo</p>  
* @author wulinfeng
* @date 2018年7月20日下午2:59:54
*/
@RestController
@SpringBootApplication
public class TraceDemoApplication {

	public static void main(String[] args) {
        SpringApplication.run(TraceDemoApplication.class, args);
    }
	
	@Trace
	@GetMapping("/server1")
	public String server1() {
		return "hello world！";
	}
	
}
