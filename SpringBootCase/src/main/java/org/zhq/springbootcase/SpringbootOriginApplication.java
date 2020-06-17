package org.zhq.springbootcase;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.zhq.springbootcase.annotation.Red;

//@SpringBootApplication(scanBasePackages = "org.zhq.springbootorigin.annotation")
//@SpringBootApplication
public class SpringbootOriginApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootOriginApplication.class, args);
        Red red = context.getBean(Red.class);
        System.out.println(red);
    }

}
