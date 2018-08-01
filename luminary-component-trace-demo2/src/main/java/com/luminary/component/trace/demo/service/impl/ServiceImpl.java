/**  
* <p>Title: ServiceImpl.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月26日上午11:11:47  
*/  
package com.luminary.component.trace.demo.service.impl;

import org.springframework.stereotype.Component;

import com.luminary.component.hystrix.tracker.HystrixTracker;
import com.luminary.component.trace.annotation.Trace;
import com.luminary.component.trace.demo.service.Service;

import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: ServiceImpl</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年7月26日上午11:11:47
*/
@Slf4j
@Component
public class ServiceImpl implements Service {

	/* (non-Javadoc)  
	 * <p>Title: server3</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see com.luminary.component.trace.demo.service.Service#server3()  
	 */
	@Trace(HystrixTracker.class)
	@Override
	public String server3() {
		log.info("hystrix");
		return "hello world3";
	}

	/* (non-Javadoc)  
	 * <p>Title: server4</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see com.luminary.component.trace.demo.service.Service#server4()  
	 */
	@Trace(HystrixTracker.class)
	@Override
	public String server4() {
		log.info("hystrix");
		return "hello world3";
	}

}
