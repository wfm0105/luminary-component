package com.luminroy.component.logger;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=LoggerDemoApplication.class)
public class DemoTest {

    @Test
    public void loggerTest() {
        log.info("测试！");
    }

}
