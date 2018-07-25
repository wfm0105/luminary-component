/**  
* <p>Title: RpcTraceInfoVO.java</p>  
* <p>Description: 封装了链路调用信息</p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日上午10:56:59  
*/  
package com.luminary.component.trace.model;

import lombok.Data;

/**  
* <p>Title: RpcTraceInfoVO</p>  
* <p>Description: 封装了链路调用信息</p>  
* @author wulinfeng
* @date 2018年7月20日上午10:56:59
*/
@Data
public class RpcTraceInfoVO {

	public static final String RESULT_SUCCESS = "OK";
	
	public static final String RESULT_FAILURE = "ERROR";
	
	private String traceId; // 链路跟踪的id
	private String rpcId; // 调用id
	private String rpcType; // 类型
	private String clientName; // 客户端名
	private String clientHost; // 客户端主机名
	private String serviceCategory; // 服务大类
	private String serviceName; // 服务名
	private String serviceHost; // 服务主机名
	private String methodName; // 方法名
	private String requestDateTime; // 请求时间
	private String requestJson; // 请求参数
	private String responseJson; // 返回参数
	private String result; // 结果
	private long runTime; // 调用耗时
	private String profile; // 环境
	
}
