package org.zhq.custom;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MySpringContext {
    private Map<Class<?>, Object> container;
    private Map<String, Method> mappings = new HashMap<>();
    private Map<String, Object> controllers = new HashMap<>();

    public MySpringContext(Map<Class<?>, Object> container) {
        this.container = container;
    }

    public void init() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //实例化bean
        for (Class<?> klass : container.keySet()) {
            if (klass.isAnnotationPresent(MyController.class)||klass.isAnnotationPresent(MyService.class)) {
                Object bean = klass.getConstructor().newInstance();
                container.put(klass, bean);
            }
        }
        //注入field
        for (Class<?> klass : container.keySet()) {
            Object bean = container.get(klass);
            for (Field field : klass.getDeclaredFields()) {
                if (field.getAnnotation(MyAutowired.class) != null) {
                    field.setAccessible(true);
                    Object fieldBean = container.get(field.getType());
                    field.set(bean, fieldBean);
                }
            }
        }
        //初始化请求映射
        for (Object bean : container.values()) {
            if (bean.getClass().getAnnotation(MyController.class) != null) {
                controllers.put(bean.getClass().getName(), bean);
                MyRequestMapping klassAnno = bean.getClass().getAnnotation(MyRequestMapping.class);
                String basePath = "";
                if (klassAnno != null) {
                    basePath = klassAnno.value();
                }
                for (Method method : bean.getClass().getMethods()) {
                    MyRequestMapping methodAnno = method.getAnnotation(MyRequestMapping.class);
                    if (methodAnno == null) continue;
                    mappings.put(basePath + methodAnno.value(), method);
                }
            }
        }
    }

    public Map<String, Object> getControllers() {
        return controllers;
    }

    public Map<String, Method> getMappings() {
        return mappings;
    }
}
