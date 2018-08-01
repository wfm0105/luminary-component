/**  
* <p>Title: TestServiceImpl.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月30日下午4:16:02  
*/  
package com.luminary.component.trace.demo.service.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luminary.component.cache.processor.SpringCacheProcessor;
import com.luminary.component.trace.demo.CacheKey;
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
	public SpringCacheProcessor cacheProcessor;
	
	@Autowired
	public TestDao testDao;
	
	/* (non-Javadoc)  
	 * <p>Title: get</p>  
	 * <p>Description: </p>  
	 * @return  
	 * @see com.luminary.component.trace.demo.service.TestService#get()  
	 */
	@Override
	public long get(long number) {
		String data = 
			cacheProcessor.getData(CacheKey.TEST_KEY, CacheKey.TEST_KEY+number, 
				key->{
					long result = testDao.selectNumber(number);
					return result+"";
			});
		
		if(StringUtils.isEmpty(data))
			return 0;
		
		return Long.valueOf(data);
	}

}
