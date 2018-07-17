package com.luminroy.component.logger.appender;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import ch.qos.logback.core.status.ErrorStatus;
import com.google.gson.Gson;
import com.luminory.component.elasticsearch.JestClientMgr;
import com.luminroy.component.logger.model.EsLogVO;
import com.luminroy.component.util.web.HostUtil;
import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import lombok.Cleanup;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * elasticsearch的appender
 *
 * @author wulinfeng
 * @date 2018/7/17 19:42
 */
@Slf4j
public class ElasticsearchAppender<E> extends UnsynchronizedAppenderBase<E> {

	private JestClient jestClient;

	private static final String CONFIG_PROPERTIES_NAME = "es.properties";

	// 可在xml中配置的属性
	@Setter
	private String esIndex = "java-log-#date#"; // 索引
	@Setter
	private String esType = "java-log";	// 类型
	@Setter
	private boolean isLocationInfo = true;	// 是否打印行号
	@Setter
	private String env = "";	// 运行环境
	@Setter
	private String esAddress = ""; //	地址

	@Override
	public void start() {
		super.start();
		init();
	}

	@Override
	public void stop() {
		super.stop();
		// 关闭es客户端
		try {
			jestClient.close();
		} catch (IOException e) {
			addStatus(new ErrorStatus("close jestClient fail", this, e));
		}
	}

    @Override
    protected void append(E event) {
    	 if (!isStarted()) {
             return;
         }

    	 subAppend(event);
    }
	
    private void subAppend(E event) {
    	if (!isStarted()) {
            return;
        }
    	
    	try {
            // this step avoids LBCLASSIC-139
            if (event instanceof DeferredProcessingAware) {
                ((DeferredProcessingAware) event).prepareForDeferredProcessing();
            }
            // the synchronization prevents the OutputStream from being closed while we
            // are writing. It also prevents multiple threads from entering the same
            // converter. Converters assume that they are in a synchronized block.
            save(event);
        } catch (Exception ioe) {
            // as soon as an exception occurs, move to non-started state
            // and add a single ErrorStatus to the SM.
            this.started = false;
            addStatus(new ErrorStatus("IO failure in appender", this, ioe));
        }
    }
    
    private void save(E event) {
    	if(event instanceof LoggingEvent) {
    		// 获得日志数据
			EsLogVO esLogVO = createData((LoggingEvent) event);
			// 保存到es中
			save(esLogVO);
    	} else {
    		addWarn("the error type of event!");
    	}
    }

	private void save(EsLogVO esLogVO) {
		Gson gson = new Gson();
		String jsonString = gson.toString();

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String esIndexFormat = esIndex.replace("#date#", sdf.format(Calendar.getInstance().getTime()));
		Index index = new Index.Builder(esLogVO).index(esIndexFormat).type(esType).build();

		try {
			DocumentResult result = jestClient.execute(index);
			log.info(result.toString());
		} catch (Exception e) {
			addStatus(new ErrorStatus("jestClient exec fail", this, e));
		}
	}

	private EsLogVO createData(LoggingEvent event) {
		EsLogVO esLogVO = new EsLogVO();

		// 获得ip
		esLogVO.setIp(HostUtil.getIP());

		// 获得hostName
		esLogVO.setHost(HostUtil.getHostName());

		// 获得时间
		String dateTime = getDateTime(event);
		esLogVO.setDateTime(dateTime);

		// 获得线程
		String threadName = getThead(event);
		esLogVO.setThread(threadName);

		// 获得日志等级
		String level = getLevel(event);
		esLogVO.setLevel(level);

		// 获得调用信息
		EsLogVO.Location location = getLocation(event);
		esLogVO.setLocation(location);

		// 获得日志信息
		String message = getMessage(event);
		esLogVO.setMessage(message);

		// 获得异常信息
		String throwable = getThrowable(event);
		esLogVO.setThrowable(throwable);

		// 获得traceId
		String traceId = getTraceId(event);
		esLogVO.setTraceId(traceId);

		// 获得rpcId
		String rpcId = getRpcId(event);
		esLogVO.setRpcId(rpcId);

		return esLogVO;
	}

	private String getRpcId(LoggingEvent event) {
		Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
		return mdcPropertyMap.get("traceId");
	}

	private String getTraceId(LoggingEvent event) {
		Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
		return mdcPropertyMap.get("traceId");
	}

	private String getThrowable(LoggingEvent event) {
		String exceptionStack = "";
		IThrowableProxy tp = event.getThrowableProxy();
		if (tp == null)
			return "";

		StringBuilder sb = new StringBuilder(2048);
		while (tp != null) {

			StackTraceElementProxy[] stackArray = tp.getStackTraceElementProxyArray();

			ThrowableProxyUtil.subjoinFirstLine(sb, tp);

			int commonFrames = tp.getCommonFrames();
			StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
			for (int i = 0; i < stepArray.length - commonFrames; i++) {
				sb.append("\n");
				sb.append(CoreConstants.TAB);
				ThrowableProxyUtil.subjoinSTEP(sb, stepArray[i]);
			}

			if (commonFrames > 0) {
				sb.append("\n");
				sb.append(CoreConstants.TAB).append("... ").append(commonFrames).append(" common frames omitted");
			}

			sb.append("\n");

			tp = tp.getCause();
		}
		return sb.toString();
	}

	private String getMessage(LoggingEvent event) {
		return event.getFormattedMessage();
	}

	private EsLogVO.Location getLocation(LoggingEvent event) {
		EsLogVO.Location location = new EsLogVO.Location();
		if(isLocationInfo) {
			StackTraceElement[] cda = event.getCallerData();
			if (cda != null && cda.length > 0) {
				StackTraceElement immediateCallerData = cda[0];
				location.setClassName(immediateCallerData.getClassName());
				location.setMethod(immediateCallerData.getMethodName());
				location.setFile(immediateCallerData.getFileName());
				location.setLine(String.valueOf(immediateCallerData.getLineNumber()));
			}
		}
		return location;
	}

	private String getLevel(LoggingEvent event) {
		return event.getLevel().toString();
	}

	private String getThead(LoggingEvent event) {
		return event.getThreadName();
	}

	private String getDateTime(LoggingEvent event) {
		long timestamp = ((LoggingEvent) event).getTimeStamp();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		return sdf.format(new Date(timestamp));
	}

    private void init() {
		try {
			ClassLoader esClassLoader = ElasticsearchAppender.class.getClassLoader();
			Set<URL> esConfigPathSet = new LinkedHashSet<URL>();
			Enumeration<URL> paths;
			if (esClassLoader == null) {
				paths = ClassLoader.getSystemResources(CONFIG_PROPERTIES_NAME);
			} else {
				paths = esClassLoader.getResources(CONFIG_PROPERTIES_NAME);
			}
			while (paths.hasMoreElements()) {
				URL path = paths.nextElement();
				esConfigPathSet.add(path);
			}

			if(esConfigPathSet.size() == 0) {
				addWarn("没有获取到配置信息！");
				// 用默认信息初始化es客户端
				jestClient = new JestClientMgr().getJestClient();
			}

			if(esConfigPathSet.size() > 1) {
				addWarn("获取到多个配置信息,将以第一个为准！");
			}

			URL path = esConfigPathSet.iterator().next();
			try {
				Properties config = new Properties();
				@Cleanup InputStream input = new FileInputStream(path.getPath());
				config.load(input);
				// 通过properties初始化es客户端
				jestClient = new JestClientMgr(config).getJestClient();
			} catch (Exception e) {
				addStatus(new ErrorStatus("config fail", this, e));
			}
		} catch (Exception e) {
			addStatus(new ErrorStatus("config fail", this, e));
		}
	}
    
}
