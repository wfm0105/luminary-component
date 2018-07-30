/**  
* <p>Title: TestServiceImpl.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月30日下午4:16:02  
*/  
package com.luminary.component.trace.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luminary.component.trace.demo.dao.TestDao;
import com.luminary.component.trace.demo.service.TestService;

/**  
* <p>Title: TestServiceImpl</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年7月30日下午4:16:02
*/
@Service
public class TestServiceImpl implements TestService {

	@Autowired
	public TestDao testDao;
	
	/* (non-Javadoc)  
	 * <p>Title: get</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see com.luminary.component.trace.demo.service.TestService#get()  
	 */
	@Override
	public long get() {
		return testDao.selectOne();
	}

}
