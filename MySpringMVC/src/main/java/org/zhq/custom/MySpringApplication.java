package org.zhq.custom;

import org.apache.catalina.LifecycleException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class MySpringApplication {
    public static void main(String[] args) throws IllegalAccessException, IOException, InstantiationException, LifecycleException {
        MySpringContext context = new MySpringContext();
        try {
            context.init();
        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        MyServer.startServer(new MyDispatcherServlet(context));
    }
}




