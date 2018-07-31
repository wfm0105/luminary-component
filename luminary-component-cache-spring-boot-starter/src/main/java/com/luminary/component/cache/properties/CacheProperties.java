/**  
* <p>Title: CacheProperties.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日下午4:35:54  
*/  
package com.luminary.component.cache.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.luminary.component.cache.expired.ExpiredConstants;

/**  
* <p>Title: CacheProperties</p>  
* <p>Description: 缓存配置</p>  
* @author wulinfeng
* @date 2018年7月31日下午4:35:54
*/
@ConfigurationProperties(prefix = "luminary.cache")
public class CacheProperties {

private int minExpiredSeconds = ExpiredConstants.MIN_EXPIRED_SECONDS;
	
	private int maxExpiredSeconds = ExpiredConstants.MAX_EXPIRED_SECONDS;
	
	private boolean disableAll = false;
	
	private String[] disableKeys = {};

	public int getMinExpiredSeconds() {
		return minExpiredSeconds;
	}

	public void setMinExpiredSeconds(int minExpiredSeconds) {
		this.minExpiredSeconds = minExpiredSeconds;
	}

	public int getMaxExpiredSeconds() {
		return maxExpiredSeconds;
	}

	public void setMaxExpiredSeconds(int maxExpiredSeconds) {
		this.maxExpiredSeconds = maxExpiredSeconds;
	}

	public boolean isDisableAll() {
		return disableAll;
	}

	public void setDisableAll(boolean disableAll) {
		this.disableAll = disableAll;
	}

	public String[] getDisableKeys() {
		return disableKeys;
	}

	public void setDisableKeys(String[] disableKeys) {
		this.disableKeys = disableKeys;
	}
	
}
