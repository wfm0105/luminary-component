package com.luminroy.component.logger.appender;

import java.io.IOException;

import com.luminory.component.elasticsearch.JestClientMgr;
import com.luminory.component.elasticsearch.config.JestConfig;

import ch.qos.logback.core.status.ErrorStatus;
import lombok.Setter;

public class SpringElasticsearchAppender<E> extends ElasticsearchAppender<E> implements LuminroyLoggerAppender<E> {

    @Setter
    protected String username;

    @Setter
    protected String password;

    @Setter
    protected String server;

    @Setter
    protected boolean multiThreaded;

    @Setter
    protected int maxTotalConnection;

    @Setter
    protected int maxTotalConnectionPerRoute;

    @Setter
    protected boolean discoveryEnabled;

    @Setter
    protected int discorveryFrequency;

    @Override
    public void subInit() {
        JestConfig jestConfig = new JestConfig();
        jestConfig.setUsername(username);
        jestConfig.setPassword(password);
        jestConfig.setServer(server.split(","));
        jestConfig.setMultiThreaded(multiThreaded);
        jestConfig.setMaxTotalConnection(maxTotalConnection);
        jestConfig.setMaxTotalConnectionPerRoute(maxTotalConnectionPerRoute);
        jestConfig.setDiscoveryEnabled(discoveryEnabled);
        jestConfig.setDiscorveryFrequency(discorveryFrequency);
        try {
            JestClientMgr jestClientMgf = new JestClientMgr(jestConfig);
            jestClient = jestClientMgf.getJestClient();
        } catch (IOException e) {
            addStatus(new ErrorStatus("config fail", this, e));
        }
    }

}
