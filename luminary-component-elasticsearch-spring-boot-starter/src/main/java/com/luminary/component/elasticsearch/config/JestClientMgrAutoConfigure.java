package com.luminary.component.elasticsearch.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.luminary.component.elasticsearch.JestClientMgr;
import com.luminary.component.elasticsearch.SpringJestClientMgr;
import com.luminary.component.elasticsearch.properties.ElasticsearchProperties;

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
