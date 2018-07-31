/**  
* <p>Title: BaseExpiredStrategy.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午3:56:13  
*/  
package com.luminary.component.cache.expired;

import java.util.Random;

/**  
* <p>Title: BaseExpiredStrategy</p>  
* <p>Description: 基于给定范围的随机超时策略</p>  
* @author wulinfeng
* @date 2018年7月31日下午3:56:13
*/
public class BaseExpiredStrategy implements ExpiredStrategy{

	private int min;
	private int max;
	
	public BaseExpiredStrategy(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public int expiredSeconds() {
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}

}
