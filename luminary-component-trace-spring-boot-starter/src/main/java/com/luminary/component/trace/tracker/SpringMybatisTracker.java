/**  
* <p>Title: SpringMybatisTracker.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月31日上午9:51:01  
*/  
package com.luminary.component.trace.tracker;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.luminary.component.trace.client.TraceClient;
import com.luminary.component.trace.context.ProfileContext;
import com.luminary.component.trace.context.TraceClientContext;

import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: SpringMybatisTracker</p>  
* <p>Description: </p>  
* @author wulinfeng
* @date 2018年7月31日上午9:51:01
*/
@Slf4j
@Intercepts({
	@Signature(type = Executor.class, args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}, method = "query"),
	@Signature(type = Executor.class, args = {MappedStatement.class, Object.class}, method = "update")
})
public class SpringMybatisTracker extends MybatisTracker {
	
	public SpringMybatisTracker() {
		super();
	}
	
	/**  
	* <p>Title: </p>  
	* <p>Description: </p>  
	* @param traceClient  
	*/  
	public SpringMybatisTracker(TraceClient traceClient) {
		super(traceClient);
	}
	
	@Override
	public String getProfile() {
		return ProfileContext.profile;
	}

	public TraceClient getTraceClient() {
		return TraceClientContext.traceClient;
	}
	
}
