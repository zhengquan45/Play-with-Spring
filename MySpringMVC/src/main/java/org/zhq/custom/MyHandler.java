package org.zhq.custom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MyHandler {
    protected Object controller;
    protected Method method;
    protected Pattern pattern;
    protected Map<String, Integer> paramIndexMapping;

    public MyHandler(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
        this.paramIndexMapping = new HashMap<>();
        putParamIndexMapping(method);
    }

    private void putParamIndexMapping(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            for (Annotation annotation : annotations) {
                if (annotation instanceof MyRequestMapping) {
                    String paramName = ((MyRequestMapping) annotation).value();
                    if (paramName.trim().equals("")) {
                        paramIndexMapping.put(paramName, i);
                    }
                }
            }
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType == HttpServletResponse.class
                    || parameterType == HttpServletRequest.class) {
                paramIndexMapping.put(parameterType.getName(),i);
            }
        }
    }
}
