package com.luminroy.component.logger;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=LoggerDemoApplication.class)
public class DemoTest {

    @Test
    public void loggerTest() throws InterruptedException {
    	 log.info("info测试！");
         log.debug("debug测试，一些调试信息！");
         log.error("错误", new RuntimeException("错误测试"));
         TimeUnit.SECONDS.sleep(6);
    }

}
