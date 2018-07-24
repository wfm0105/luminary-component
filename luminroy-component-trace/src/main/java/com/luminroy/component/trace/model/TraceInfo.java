/**  
* <p>Title: TraceInfo.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日上午11:44:16  
*/  
package com.luminroy.component.trace.model;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import com.luminroy.component.util.common.UUIDUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: TraceInfo</p>  
* <p>Description: 封装了链路调用基本信息与逻辑</p>  
* @author wulinfeng
* @date 2018年7月20日上午11:44:16
*/
@Slf4j
@Data
public class TraceInfo {

	public static final String TRACE_ID_KEY = "traceId";
	
	public static final String RPC_ID_KEY = "rpcId";
	
	/**
	 * 
	 * 链路跟踪的id
	 * 
	 */
	private String traceId;
	
	/**
	 * 
	 * 层级
	 * 比如：rpcId=1.1.1 hierarchy=1.1
	 * 
	 */
	private String hierarchy;
	
	/**
	 * 
	 * 当前级别序号 
	 * 比如：rpcId=1.1.1 sequenceNo=1
	 * 
	 */
	private AtomicInteger sequenceNo;
	
	public TraceInfo() {
		this.traceId = UUIDUtil.uuid32();
		this.hierarchy = "";
		this.sequenceNo = new AtomicInteger(1);
		log.info(String.format("new traceInfo hierarchy=%s, sequenceNo=%s", this.hierarchy, this.sequenceNo));
	}
	
	public TraceInfo(String traceId, String rpcId) {
		this.traceId = traceId;
		setRpcId(rpcId);
		log.info(String.format("new traceInfo hierarchy=%s, rpcId=%s", this.hierarchy, rpcId));
	}
	
	/**
	 * 
	 * <p>Title: addSequenceNo</p>  
	 * <p>Description: 增加级别序号</p>  
	 * @return
	 */
	public TraceInfo addSequenceNo() {
		this.sequenceNo.incrementAndGet();
		log.info(String.format("addSequenceNo hierarchy=%s, sequenceNo=%s", this.hierarchy, this.sequenceNo));
		return this;
	}
	
	/**
	 * 
	 * <p>Title: addHierarchy</p>  
	 * <p>Description: 增加层级</p>  
	 * @return
	 */
	public TraceInfo addHierarchy() {
		this.hierarchy = getRpcId();
		this.sequenceNo = new AtomicInteger(0);
		log.info(String.format("addHierarchy hierarchy=%s, sequenceNo=%s", this.hierarchy, this.sequenceNo));
		return this;
	}
	
	/**
	 * 
	 * <p>Title: setRpcId</p>  
	 * <p>Description: 设置rpcId</p>  
	 * @param rpcId
	 */
	public void setRpcId(String rpcId) {
		int lastDotIndex = rpcId.lastIndexOf(".");
		if(lastDotIndex>-1) {
			this.hierarchy = (String) rpcId.subSequence(0, lastDotIndex);
			this.sequenceNo = new AtomicInteger(Integer.valueOf(rpcId.substring(lastDotIndex + 1)));
		} else {
			this.hierarchy = "";
			this.sequenceNo = new AtomicInteger(Integer.valueOf(rpcId));
		}
		log.info(String.format("setRpcId hierarchy=%s, sequenceNo=%s", this.hierarchy, this.sequenceNo));
	}
	
	/**
	 * 
	 * <p>Title: getRpcId</p>  
	 * <p>Description: 获得rpcId</p>  
	 * @return
	 */
	public String getRpcId() {
		if(StringUtils.isBlank(this.hierarchy)) {
			return sequenceNo.get()+"";
		}
		return this.hierarchy+"."+sequenceNo.get();
	}
	
	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("TraceInfo{");
		sb.append("traceId='").append(traceId).append("\'");
		sb.append(", rpcId='").append(getRpcId());
		sb.append("}");
		return sb.toString();
	}
	
}
