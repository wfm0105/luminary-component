package com.luminary.component.elasticsearch;

import org.springframework.beans.BeanUtils;

import com.luminary.component.elasticsearch.config.JestConfig;
import com.luminary.component.elasticsearch.properties.ElasticsearchProperties;

public class SpringJestClientMgr extends JestClientMgr {

    public SpringJestClientMgr(ElasticsearchProperties elasticsearchProperties) {
        JestConfig jestConfig = new JestConfig();
        BeanUtils.copyProperties(elasticsearchProperties, jestConfig);
        config = jestConfig;
    }

}
