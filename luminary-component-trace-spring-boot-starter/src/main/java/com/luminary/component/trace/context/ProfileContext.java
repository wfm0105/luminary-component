/**  
* <p>Title: ContextHelper.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日上午10:40:43  
*/  
package com.luminary.component.trace.context;

/**  
* <p>Title: ContextHelper</p>  
* <p>Description: 非spring管理的bean中用来获取profile</p>  
* @author wulinfeng
* @date 2018年7月31日上午10:40:44
*/
public class ProfileContext {
	
	public static String profile;

	public void setProfile(String profile) {
		ProfileContext.profile = profile;
	}
	
}
