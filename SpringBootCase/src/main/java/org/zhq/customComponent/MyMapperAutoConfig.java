package org.zhq.customComponent;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MyMapperAutoConfiguredMybatisRegistrar.class)
public class MyMapperAutoConfig {
}
