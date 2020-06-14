package org.zhq.custom;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MyComponent
public @interface MyService {
    String value() default "";
}
