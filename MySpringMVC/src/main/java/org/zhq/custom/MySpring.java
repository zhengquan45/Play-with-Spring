package org.zhq.custom;


import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MySpring {

    public static Map<Class<?>, Object> doScanner() throws IOException, IllegalAccessException, InstantiationException {
        Map<Class<?>, Object> container = new HashMap<>();
        ClassPath classpath = ClassPath.from(MySpringApplication.class.getClassLoader());
        //扫描某个包的所有类Class
        List<Class<?>> componentClasses = classpath.getTopLevelClassesRecursive(MySpringApplication.class.getPackage().getName())
                .stream().map(ClassPath.ClassInfo::getName).map(name -> {
                    try {
                        return Class.forName(name);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(klass -> klass.getAnnotation(MyController.class) != null || klass.getAnnotation(MyService.class) != null)
                .collect(Collectors.toList());
        for (Class<?> klass : componentClasses) {
            container.put(klass, null);
        }
        return container;
    }







}
