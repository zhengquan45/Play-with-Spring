package org.zhq.custom;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
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

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, InvocationTargetException, IllegalAccessException, URISyntaxException {
        String uri = req.getRequestURI();
        Map<String, Method> mappings = mySpringContext.getMappings();
        Map<String, Object> controllers = mySpringContext.getControllers();
        if (!mappings.containsKey(uri)) {
            resp.getWriter().write("404 NOT FOUND");
            return;
        }
        Method method = mappings.get(uri);
        Object controller = controllers.get(method.getDeclaringClass().getName());

        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] parameters = new Object[paramTypes.length];

        // 参数注入
        for (int i = 0; i < paramTypes.length; ++i) {
            Class<?> paramType = paramTypes[i];
            if (paramType == HttpServletRequest.class) {
                parameters[i] = req;
            } else if (paramType == HttpServletResponse.class) {
                parameters[i] = resp;
            } else {
                Annotation[] annosOnParam = method.getParameterAnnotations()[i];
                for (Annotation annoOnParam : annosOnParam) {
                    if (annoOnParam.annotationType() == MyRequestParam.class) {
                        parameters[i] = convert(paramType, req.getParameter(((MyRequestParam) annoOnParam).value()));
                        break;
                    }
                    if (annoOnParam.annotationType() == MyRequestBody.class) {
                        byte[] bytes = binaryReader(req);
                        parameters[i] = JSONUtil.toBean(new String(bytes),paramType);
                        break;
                    }
                }
            }
        }
        Object mv = method.invoke(controller, parameters);
        if (mv instanceof ModelAndView) {
            File template = new File(getClass().getResource("/view/" + ((ModelAndView) mv).getViewName() + ".mytemplate").toURI());
            String content = new String(Files.readAllBytes(template.toPath()));
            for (Map.Entry<String, Object> entry : ((ModelAndView) mv).getModelMap().entrySet()) {
                String k = entry.getKey();
                Object v = entry.getValue();
                content = content.replace("${" + k + "}", v.toString());
            }
            resp.setHeader("Content-Type", "text/html");
            resp.getOutputStream().print(content);
            resp.getOutputStream().flush();
            return;
        } else if (method.getAnnotation(MyResponseBody.class) != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(mv);
            resp.setHeader("Content-Type", "application/json");
            resp.getOutputStream().print(json);
            resp.getOutputStream().flush();
            return;
        }
    }

    private Object convert(Class<?> paramType, String parameter) {
        return ConvertFactory.getConvert(paramType).doConvert(parameter);
    }

    private byte[] binaryReader(HttpServletRequest request) throws IOException {
        int len = request.getContentLength();
        ServletInputStream iii = request.getInputStream();
        byte[] buffer = new byte[len];
        iii.read(buffer, 0, len);
        return buffer;
    }

}
