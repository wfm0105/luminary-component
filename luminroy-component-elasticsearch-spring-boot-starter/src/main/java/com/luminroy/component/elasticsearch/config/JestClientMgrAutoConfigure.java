package com.luminroy.component.elasticsearch.config;

import com.luminory.component.elasticsearch.JestClientMgr;
import com.luminroy.component.elasticsearch.SpringJestClientMgr;
import com.luminroy.component.elasticsearch.properties.ElasticsearchProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(JestClientMgr.class)
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class JestClientMgrAutoConfigure {

    @Autowired
    private ElasticsearchProperties elasticsearchProperties;

    @Bean
    @ConditionalOnMissingBean
    public JestClientMgr jestClientMgr() {
        SpringJestClientMgr springJestClientMgr = new SpringJestClientMgr(elasticsearchProperties);
        return springJestClientMgr;
    }

}
