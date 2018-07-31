/**  
* <p>Title: ExpiredStrategy.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午3:56:35  
*/  
package com.luminary.component.cache.expired;

/**  
* <p>Title: ExpiredStrategy</p>  
* <p>Description: 超时策略</p>  
* @author wulinfeng
* @date 2018年7月31日下午3:56:35
*/
public interface ExpiredStrategy {

	public int expiredSeconds();
	
}
