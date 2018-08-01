/**  
* <p>Title: ElasticsearchTranceClient.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018-2099</p>  
* <p>Company: </p>  
* @author wulinfeng  
* @date 2018年7月20日下午1:35:18  
*/  
package com.luminary.component.trace.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.luminary.component.elasticsearch.JestClientMgr;
import com.luminary.component.trace.config.TraceConfig;
import com.luminary.component.trace.model.RpcTraceInfoVO;
import com.luminary.component.util.common.DateUtil;

import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**  
* <p>Title: ElasticsearchTranceClient</p>  
* <p>Description: 使用elasticsearch进行链路跟踪结果处理</p>  
* @author wulinfeng
* @date 2018年7月20日下午1:35:18
*/
@Slf4j
public class ElasticsearchTraceClient implements TraceClient {

	 private static final String CONFIG_PROPERTIES_NAME = "trace.properties";
	
	private static JestClient jestClient;
	private static ExecutorService pool = Executors.newFixedThreadPool(10);
	
	private TraceConfig traceConfig;
	
	public ElasticsearchTraceClient() {
		try { 
			 Properties properties = new Properties();
	         @Cleanup InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_PROPERTIES_NAME);
	         if(input == null) {
	        	 // 使用默认配置
	        	 log.warn("使用默认的配置！");
	        	 this.traceConfig = new TraceConfig();
	         } else {
				 // 通过配置文件加载配置
	             properties.load(input);
	             configByProperties(properties);
	         }
	         this.traceConfig.setEsIndex(this.traceConfig.getEsIndex().replace("${date}", DateUtil.formatYearMonthDay(Calendar.getInstance().getTime())));
		} catch (Exception e) {
			log.error("ElasticsearchTranceClient实例化失败！", e);
		}
	}
	
	public ElasticsearchTraceClient(TraceConfig traceConfig) {
		if(traceConfig == null) {
			log.warn("使用默认的配置！");
			this.traceConfig = new TraceConfig();
		} else {
			this.traceConfig = traceConfig;
		}
		this.traceConfig.setEsIndex(this.traceConfig.getEsIndex().replace("${date}", DateUtil.formatYearMonthDay(Calendar.getInstance().getTime())));
	}
	
	public ElasticsearchTraceClient(TraceConfig traceConfig, JestClient jestClient) {
		this(traceConfig);
		ElasticsearchTraceClient.jestClient = jestClient;
	}

	public ElasticsearchTraceClient(@NonNull String path) throws IOException {
        Properties properties = new Properties();
        @Cleanup InputStream input = new FileInputStream(new File(path));
        properties.load(input);

        configByProperties(properties);
        this.traceConfig.setEsIndex(this.traceConfig.getEsIndex().replace("${date}", DateUtil.formatYearMonthDay(Calendar.getInstance().getTime())));
    }

    public ElasticsearchTraceClient(@NonNull Properties properties) {
        configByProperties(properties);
        this.traceConfig.setEsIndex(this.traceConfig.getEsIndex().replace("${date}", DateUtil.formatYearMonthDay(Calendar.getInstance().getTime())));
    }
	
	@Override
	public void sendTraceInfo(final RpcTraceInfoVO rpcTraceInfoVO) throws Exception {
		init();
		pool.submit(new Runnable() {

			@Override
			public void run() {
				Gson gson = new Gson();
				Index index = new Index.Builder(gson.toJson(rpcTraceInfoVO)).index(traceConfig.getEsIndex()).type(traceConfig.getEsType()).build();
				try {
					DocumentResult result = jestClient.execute(index);
					log.info("链路跟踪结果:"+result.getJsonString());
				} catch (Exception e) {
					log.error("链路跟踪结果处理失败！", e);
				}
			}
			
		});
	}
	
	private void init() throws IOException {
		if(jestClient == null) {
			jestClient = new JestClientMgr().getJestClient();
		}
	}
	
	 private void configByProperties(Properties properties) {
		 TraceConfig traceConfig = new TraceConfig();
		 
		 Optional.ofNullable(
	                properties.get("es.index")
	        ).ifPresent(value->{
	        	traceConfig.setEsIndex((String) value); 
	        });
		 
		 Optional.ofNullable(
	                properties.get("es.type")
	        ).ifPresent(value->{
	        	traceConfig.setEsType((String) value); 
	        });
		 
		 this.traceConfig = traceConfig;
	 }
	
}
