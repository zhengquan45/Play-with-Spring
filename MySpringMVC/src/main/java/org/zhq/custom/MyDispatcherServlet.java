package org.zhq.custom;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class MyDispatcherServlet extends HttpServlet {

    private MySpringContext mySpringContext;

    public MyDispatcherServlet(MySpringContext mySpringContext) {
        this.mySpringContext = mySpringContext;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Exception " + Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, InvocationTargetException, IllegalAccessException {
        String uri = req.getRequestURI();
        Map<String, Method> mappings = mySpringContext.getMappings();
        Map<String, Object> controllers = mySpringContext.getControllers();
        if (!mappings.containsKey(uri)) {
            resp.getWriter().write("404 NOT FOUND");
            return;
        }
        Method method = mappings.get(uri);
        Object controller = controllers.get(method.getDeclaringClass().getName());

        Class[] paramTypes = method.getParameterTypes();
        Object[] parameters = new Object[paramTypes.length];

        // 参数注入
        for (int i = 0; i < paramTypes.length; ++i) {
            Class paramType = paramTypes[i];
            if (paramType == HttpServletRequest.class) {
                parameters[i] = req;
            } else if (paramType == HttpServletResponse.class) {
                parameters[i] = resp;
            } else {
                Annotation[] annosOnParam = method.getParameterAnnotations()[i];
                for (Annotation annoOnParam : annosOnParam) {
                    if (annoOnParam.annotationType() == MyRequestParam.class) {
                        parameters[i] = req.getParameter(((MyRequestParam) annoOnParam).value());
                        break;
                    }
                }
            }
        }
        Object result = method.invoke(controller, parameters);

    }

}
