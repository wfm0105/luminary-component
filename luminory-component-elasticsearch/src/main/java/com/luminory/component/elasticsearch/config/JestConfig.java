package com.luminory.component.elasticsearch.config;

import lombok.Data;

/**
 *
 * jestClient配置类
 *
 * @author wulinfeng
 * @date 2018/7/17 16:17
 */
@Data
public class JestConfig {

    private String username = "elastic";
    private String password = "123456";
    private String[] server = {"127.0.0.1:9200"};
    private boolean multiThreaded = true;
    private int maxTotalConnection = 20;
    private int maxTotalConnectionPerRoute = 5;
    private boolean discoveryEnabled = true;
    private int discorveryFrequency = 60;

}
