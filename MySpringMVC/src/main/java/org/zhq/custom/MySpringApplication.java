package org.zhq.custom;

import org.apache.catalina.LifecycleException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class MySpringApplication {
    public static void main(String[] args) throws IllegalAccessException, IOException, InstantiationException, LifecycleException {
        Map<Class<?>, Object> container = MySpring.doScanner();
        MySpringContext context = new MySpringContext(container);
        try {
            context.init();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        MyServer.startServer(new MyDispatcherServlet(context));
    }
}




