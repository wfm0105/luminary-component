package com.luminary.component.logger;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ElasticsearchAppenderTest {

    @Test
    public void loggerTest() throws InterruptedException {
        log.info("info测试！");
        log.debug("debug测试，一些调试信息！");
        log.error("错误", new RuntimeException("错误测试"));
        TimeUnit.SECONDS.sleep(6);
    }

}
