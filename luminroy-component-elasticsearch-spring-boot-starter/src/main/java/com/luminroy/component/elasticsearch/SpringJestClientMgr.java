package com.luminroy.component.elasticsearch;

import com.luminory.component.elasticsearch.JestClientMgr;
import com.luminory.component.elasticsearch.config.JestConfig;
import com.luminroy.component.elasticsearch.properties.ElasticsearchProperties;
import org.springframework.beans.BeanUtils;

public class SpringJestClientMgr extends JestClientMgr {

    public SpringJestClientMgr(ElasticsearchProperties elasticsearchProperties) {
        JestConfig jestConfig = new JestConfig();
        BeanUtils.copyProperties(elasticsearchProperties, jestConfig);
        config = jestConfig;
    }

}
