package org.zhq.importBeanCase;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

@SpringBootTest
@ContextConfiguration(classes = {HelloConfiguration.class})
class HelloServiceTest {
    @Resource
    HelloService helloService;

    @Test
    void contextLoads() {
        System.out.println(helloService.getClass());
    }
}