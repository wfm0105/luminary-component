package com.luminory.component.elasticsearch;

import com.luminory.component.elasticsearch.config.JestConfig;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *
 * 生成jestClient的工具
 *
 * @author wulinfeng
 * @date 2018/7/17 16:15
 */
@Slf4j
public class JestClientMgr {

    private static final String CONFIG_PROPERTIES_NAME = "es.properties";

    private JestClient jestClient;
    private JestConfig config;

    public JestClientMgr() {

    }

    public JestClientMgr(JestConfig config) {
        this.config = config;
    }

    public JestClientMgr(@NonNull String path) throws IOException {
        Properties properties = new Properties();
        @Cleanup InputStream input = new FileInputStream(new File(path));
        properties.load(input);

        configByProperties(properties);
    }

    public JestClientMgr(@NonNull Properties properties) {
        configByProperties(properties);
    }

    private void init() throws IOException {
        if(jestClient == null) {
            synchronized (JestClientMgr.class) {
                if(jestClient == null) {
                    // 检查配置
                    if(config == null) {
                        Properties properties = new Properties();
                        @Cleanup InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_PROPERTIES_NAME);
                        if(input == null) {
                            this.config = new JestConfig();
                        } else {
                            properties.load(input);
                            configByProperties(properties);
                        }
                    }

                    // server必须是http开头的ip地址
                    List<String> serverList = new ArrayList<String>();
                    String[] server = this.config.getServer();
                    Arrays.asList(server)
                          .stream()
                          .forEach(item->{
                              if(!item.startsWith("http://")) {
                                  item = "http://"+item;
                              }
                              serverList.add(item);
                          });

                    // 创建jestClient
                    JestClientFactory factory = new JestClientFactory();
                    factory.setHttpClientConfig(
                            new HttpClientConfig.Builder(serverList)
                                    .multiThreaded(config.isMultiThreaded())
                                    .maxTotalConnection(config.getMaxTotalConnection())
                                    .defaultMaxTotalConnectionPerRoute(config.getMaxTotalConnectionPerRoute())
                                    .defaultCredentials(config.getUsername(), config.getPassword())
                                    .discoveryEnabled(config.isDiscoveryEnabled())
                                    .discoveryFrequency(config.getDiscorveryFrequency(), TimeUnit.SECONDS)
                                    .build()
                    );

                    jestClient = factory.getObject();
                }
            }
        }
    }

    public JestClient getJestClient() throws IOException {
        if(jestClient == null)
            init();
        return jestClient;
    }

    private void configByProperties(Properties properties) {
        JestConfig config = new JestConfig();

        Optional.ofNullable(
                properties.get("es.username")
        ).ifPresent(value->{
            config.setUsername((String) value);
        });

        Optional.ofNullable(
                properties.get("es.password")
        ).ifPresent(value->{
            config.setPassword((String) value);
        });

        Optional.ofNullable(
                properties.get("es.server")
        ).ifPresent(value->{
            String[] values = ((String) value).split(",");
            config.setServer(values);
        });

        Optional.ofNullable(
                properties.get("es.multiThreaded")
        ).ifPresent(value->{
            config.setMultiThreaded((boolean) value);
        });

        Optional.ofNullable(
                properties.get("es.maxTotalConnection")
        ).ifPresent(value->{
            config.setMaxTotalConnection((int) value);
        });

        Optional.ofNullable(
                properties.get("es.maxTotalConnectionPerRoute")
        ).ifPresent(value->{
            config.setMaxTotalConnectionPerRoute((int) value);
        });

        Optional.ofNullable(
                properties.get("es.discoveryEnabled")
        ).ifPresent(value->{
            config.setDiscoveryEnabled((boolean) value);
        });

        Optional.ofNullable(
                properties.get("es.discorveryFrequency")
        ).ifPresent(value->{
            config.setDiscorveryFrequency((int) value);
        });
    }

}
