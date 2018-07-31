/**  
* <p>Title: RelayExpiredStrategy.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午3:58:07  
*/  
package com.luminary.component.cache.expired;

import java.util.Random;

/**  
* <p>Title: RelayExpiredStrategy</p>  
* <p>Description: 基于固定延时的给定范围随机超时策略</p>  
* @author wulinfeng
* @date 2018年7月31日下午3:58:07
*/
public class RelayExpiredStrategy implements ExpiredStrategy {

	private int fix;
	private int relayMin;
	private int relayMax;
	
	public RelayExpiredStrategy(int fix, int relayMin, int relayMax) {
		this.fix = fix;
		this.relayMin = relayMin;
		this.relayMax = relayMax;
	}
	
	@Override
	public int expiredSeconds() {
		Random random = new Random();
		return fix + random.nextInt(relayMax - relayMin + 1) + relayMin;
	}

}

