/**  
* <p>Title: LuminaryRibbonClient.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月27日下午3:17:51  
*/  
package com.luminary.component.ribbon.client;

import org.springframework.cloud.netflix.ribbon.RibbonClients;

import com.luminary.component.ribbon.config.LuminaryRibbonConfiguration;

/**  
* <p>Title: LuminaryRibbonClient</p>  
* <p>Description: 使用LuminaryRibbonConfiguration配置的ribbonClient</p>  
* @author wulinfeng
* @date 2018年7月27日下午3:17:51
*/
@RibbonClients(defaultConfiguration = LuminaryRibbonConfiguration.class)
public class LuminaryRibbonClient {

}
