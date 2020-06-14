package org.zhq.custom;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MyComponent
public @interface MyController {
    String value() default "";
}
