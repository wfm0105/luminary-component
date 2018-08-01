/**  
* <p>Title: TraceInfo.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日上午11:44:16  
*/  
package com.luminary.component.trace.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import com.luminary.component.util.common.UUIDUtil;

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

	public static final String ORIGINAL_ROOT_RPC_ID = "1";
	
	public static final String RE_ORIGINAL_ROOT_RPC_ID = "1.0";
	
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
	
	/**
	 * 
	 * 跨服务的时候请求入口的rpcId
	 * 
	 */
	private String rootRpcId;
	
	/**
	 * 
	 * rpcId的缓存
	 * 
	 */
	private Map<String, Boolean> rpcIdCache = new ConcurrentHashMap<String, Boolean>();  
	
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
		log.info(String.format("before addSequenceNo hierarchy=%s, sequenceNo=%s", this.hierarchy, this.sequenceNo));
		this.sequenceNo.incrementAndGet();
		log.info(String.format("after addSequenceNo hierarchy=%s, sequenceNo=%s", this.hierarchy, this.sequenceNo));
		return this;
	}
	
	/**
	 * 
	 * <p>Title: addHierarchy</p>  
	 * <p>Description: 增加层级</p>  
	 * @return
	 */
	public TraceInfo addHierarchy() {
		log.info(String.format("before addHierarchy hierarchy=%s, sequenceNo=%s", this.hierarchy, this.sequenceNo));
		this.hierarchy = getRpcId();
		this.sequenceNo = new AtomicInteger(0);
		log.info(String.format("after addHierarchy hierarchy=%s, sequenceNo=%s", this.hierarchy, this.sequenceNo));
		return this;
	}
	
	/**
	 * 
	 * <p>Title: addHierarchy</p>  
	 * <p>Description: 减少层级， 比如1.2.1，结果为hierarchy:1，sequenceNo:2</p>  
	 * @return
	 */
	public TraceInfo subHierarchy() {
		log.info(String.format("before subHierarchy hierarchy=%s, sequenceNo=%s", this.hierarchy, this.sequenceNo));
		String oldRpcId = getRpcId();
		
		if("1".equals(oldRpcId)) {
			log.info(String.format("after subHierarchy hierarchy=%s, sequenceNo=%s", this.hierarchy, this.sequenceNo));
			return this;
		}
		
		int lastDotIndex = oldRpcId.lastIndexOf("."); 
		String oldHierarchy = oldRpcId.substring(0, lastDotIndex);
		lastDotIndex = oldHierarchy.lastIndexOf("."); 
		
		String newHierarchy = "1";
		String newSequenceNoStr = "";
		if(lastDotIndex != -1) {
			newHierarchy = oldHierarchy.substring(0, lastDotIndex);
			newSequenceNoStr = oldHierarchy.substring(lastDotIndex + 1);
		}
		
		this.hierarchy = newHierarchy;
		if(!"".equals(newSequenceNoStr))
			this.sequenceNo = new AtomicInteger(Integer.valueOf(newSequenceNoStr));
		else
			this.sequenceNo = new AtomicInteger(0);
		log.info(String.format("after subHierarchy hierarchy=%s, sequenceNo=%s", this.hierarchy, this.sequenceNo));
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
	
	/**
	 * 
	 * <p>Title: cache</p>  
	 * <p>Description: 保存rpcId缓存</p>
	 */
	public void cache() {
		rpcIdCache.put(getRpcId(), true);
	}
	
	/**
	 * 
	 * <p>Title: hasCache</p>  
	 * <p>Description: 是否有缓存</p>  
	 * @param rpcId
	 */
	public boolean hasCache(String rpcId) {
		return rpcIdCache.get(rpcId);
	}
	
	/**
	 * 
	 * <p>Title: getHierarchyMaxSeqNo</p>  
	 * <p>Description: 获得指定层级的虽大序列号</p>  
	 * @param level
	 * @return
	 */
	public int getHierarchyMaxSeqNo(int level) {
		if(level <= 0)
			return -1;
		
		List<Integer> seq = new ArrayList<Integer>();
		for(String key : rpcIdCache.keySet()) {
	    	if(key.split("[.]").length == (level+1)) {
	    		int lastDotIndex = key.lastIndexOf("."); 
	    		seq.add(Integer.valueOf(key.substring(lastDotIndex+1)));
	    	}
	    }

	   return 
	    	seq.stream()
	        .sorted(Comparator.reverseOrder())
	        .findFirst()
	        .orElse(0);
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
