package org.zhq.springbootcase;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zhq.springbootcase.condition.ConditionConfiguration;

import java.util.stream.Stream;

public class MainApp {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ConditionConfiguration.class);
        String[] beanDefinitionNames = ctx.getBeanDefinitionNames();
        Stream.of(beanDefinitionNames).forEach(System.out::println);
    }
}
