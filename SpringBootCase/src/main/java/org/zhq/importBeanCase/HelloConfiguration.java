package org.zhq.importBeanCase;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("org.zhq.importBeanCase")
@Import(HelloImportBeanDefinitionRegistrar.class)
public class HelloConfiguration {
}
