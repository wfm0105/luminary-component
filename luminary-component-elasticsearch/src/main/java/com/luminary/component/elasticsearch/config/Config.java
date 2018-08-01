package com.luminary.component.elasticsearch.config;

import lombok.Data;

/**
 *
 * elasticsearch配置类
 *
 * @author wulinfeng
 * @date 2018/7/17 16:16
 */
@Data
public class Config {
	
	private String clusterName = "elasticsearch";
	private String username = "elastic";
	private String password = "123456";
	private boolean xpack = false;
	private String[] hostName = {"localhost"};
	private int connectTimeout = 5000;
	private int socketTimeout = 60000;
	private int maxRetryTimeout = 60000;
	private int sniffInterval = 60000;
	private int sniffAfterFailureDelay = 60000;
	
}
