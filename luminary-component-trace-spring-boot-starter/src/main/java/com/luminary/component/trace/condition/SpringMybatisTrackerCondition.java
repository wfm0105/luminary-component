/**  
* <p>Title: SpringMybatisTrackerCondition.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年8月1日下午3:35:53  
*/  
package com.luminary.component.trace.condition;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**  
* <p>Title: SpringMybatisTrackerCondition</p>  
* <p>Description: 注入基于mybatis的链路跟踪器的条件</p>  
* @author wulinfeng
* @date 2018年8月1日下午3:35:53
*/
public class SpringMybatisTrackerCondition implements Condition {

	/* (non-Javadoc)  
	 * <p>Title: matches</p>  
	 * <p>Description: </p>  
	 * @param context
	 * @param metadata
	 * @return  
	 * @see org.springframework.context.annotation.Condition#matches(org.springframework.context.annotation.ConditionContext, org.springframework.core.type.AnnotatedTypeMetadata)  
	 */
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String isDisable = context.getEnvironment().getProperty("luminary.trace.disableMybatisTracker");
		if(StringUtils.isEmpty(isDisable) || "false".equals(isDisable))
			return true;
		return false;
	}

}
