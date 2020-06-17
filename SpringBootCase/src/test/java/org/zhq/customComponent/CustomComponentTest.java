package org.zhq.customComponent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = MyMapperAutoConfig.class)
class CustomComponentTest {
    @Resource
    private CountryMapper countryMapper;

    @Test
    void contextLoad() {
        System.out.println(countryMapper.getClass());
    }
}