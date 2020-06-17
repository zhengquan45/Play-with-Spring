package org.zhq.custom;

import com.google.common.reflect.ClassPath;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class MySpringContext {
    private Map<Class<?>, Object> container = new HashMap<>();
    private List<Class<?>> klassList;
    private List<MyHandler> handlerMapping = new ArrayList<>();

    public void init() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        //扫描
        doScanner();
        //实例化bean
        doInstance();
        //注入field
        doAutowired();
        //初始化请求映射
        initHandlerMapping();
    }

    private void initHandlerMapping() {
        for (Object bean : container.values()) {
            if (bean.getClass().getAnnotation(MyController.class) != null) {
                MyRequestMapping klassAnno = bean.getClass().getAnnotation(MyRequestMapping.class);
                String basePath = "";
                if (klassAnno != null) {
                    basePath = klassAnno.value();
                }
                for (Method method : bean.getClass().getMethods()) {
                    MyRequestMapping methodAnno = method.getAnnotation(MyRequestMapping.class);
                    if (methodAnno == null) continue;
                    String regex = ("/" + basePath + methodAnno.value()).replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);
                    handlerMapping.add(new MyHandler(bean, method, pattern));
                    System.out.println("Mapping " + regex + "," + method);
                }
            }
        }
    }

    private void doAutowired() throws IllegalAccessException {
        if(container.size()==0)return;
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
    }

    private void doInstance() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (klassList == null || klassList.size() == 0) return;
        for (Class<?> klass : container.keySet()) {
            if (klass.isAnnotationPresent(MyController.class) || klass.isAnnotationPresent(MyService.class)) {
                Object bean = klass.getConstructor().newInstance();
                container.put(klass, bean);
                System.out.println("Loaded " + klass.getName() + "to Ioc");
            }
        }
    }

    private void doScanner() throws IOException {
        ClassPath classpath = ClassPath.from(MySpringApplication.class.getClassLoader());
        //扫描某个包的所有类Class
        klassList = classpath.getTopLevelClassesRecursive(MySpringApplication.class.getPackage().getName())
                .stream().map(ClassPath.ClassInfo::getName).map(name -> {
                    try {
                        return Class.forName(name);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(klass -> klass.getAnnotation(MyController.class) != null || klass.getAnnotation(MyService.class) != null)
                .collect(Collectors.toList());
    }

    public MyHandler getHandler(HttpServletRequest req) {
        if(handlerMapping.isEmpty())return null;
        String uri = req.getRequestURI();
        for (MyHandler handler : handlerMapping) {
            Matcher matcher = handler.pattern.matcher(uri);
            if(matcher.matches())return handler;
        }
        return null;
    }
}
