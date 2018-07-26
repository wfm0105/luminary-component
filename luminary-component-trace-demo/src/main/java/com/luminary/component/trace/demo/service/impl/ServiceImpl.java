/**  
* <p>Title: ServiceImpl.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月25日下午5:00:39  
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
* @date 2018年7月25日下午5:00:39
*/
@Slf4j
@Component
public class ServiceImpl implements Service {

	/* (non-Javadoc)  
	 * <p>Title: feignServer2</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see com.luminary.component.trace.demo.service.Service#feignServer2()  
	 */
	@Trace(HystrixTracker.class)
	@Override
	public String feignServer2() {
		log.info("hystrix");
		return "hello world2";
	}

	/* (non-Javadoc)  
	 * <p>Title: feignServer3</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see com.luminary.component.trace.demo.service.Service#feignServer3()  
	 */
	@Trace(HystrixTracker.class)
	@Override
	public String feignServer3() {
		log.info("hystrix");
		return "hello world3";
	}

}
