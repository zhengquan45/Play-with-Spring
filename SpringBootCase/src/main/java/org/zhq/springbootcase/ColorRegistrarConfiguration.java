package org.zhq.springbootcase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zhq.springbootcase.annotation.Yellow;

@Configuration
public class ColorRegistrarConfiguration {

    @Bean
    public Yellow yellow() {
        return new Yellow();
    }

}