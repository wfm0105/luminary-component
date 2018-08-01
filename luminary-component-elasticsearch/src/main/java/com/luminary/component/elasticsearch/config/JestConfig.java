package com.luminary.component.elasticsearch.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * jestClient配置类
 *
 * @author wulinfeng
 * @date 2018/7/17 16:17
 */
public class JestConfig {

    public static final String DEFAULT_USER_NAME = "elastic";
    public static final String DEFAULT_PASSWORD = "123456";
    public static final String[] DEFAULT_SERVER = {"127.0.0.1:9200"};
    public static final boolean DEFAULT_MULTI_THREADED = true;
    public static final int DEFAULT_MAX_TOTAL_CONNECTION = 20;
    public static final int DEFAULT_MAX_TOTAL_CONNECTION_PER_ROUTE = 5;
    public static final boolean DEFAULT_DISCOVERY_ENABLED = true;
    public static final int DISCORVERY_FREQUENCY = 60;

    @Setter
    @Getter
    private String username = DEFAULT_USER_NAME;

    @Setter
    @Getter
    private String password = DEFAULT_PASSWORD;

    @Setter
    @Getter
    private String[] server = DEFAULT_SERVER;

    @Setter
    @Getter
    private boolean multiThreaded = DEFAULT_MULTI_THREADED;

    @Setter
    @Getter
    private int maxTotalConnection = DEFAULT_MAX_TOTAL_CONNECTION;

    @Setter
    @Getter
    private int maxTotalConnectionPerRoute = DEFAULT_MAX_TOTAL_CONNECTION_PER_ROUTE;

    @Setter
    @Getter
    private boolean discoveryEnabled = DEFAULT_DISCOVERY_ENABLED;

    @Setter
    @Getter
    private int discorveryFrequency = DISCORVERY_FREQUENCY;

}
