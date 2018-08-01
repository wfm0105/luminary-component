/**  
* <p>Title: TestDao.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月30日下午4:17:11  
*/  
package com.luminary.component.trace.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**  
* <p>Title: TestDao</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年7月30日下午4:17:11
*/
@Mapper
public interface TestDao {

	long selectNumber(@Param("number") long number);
	
}
