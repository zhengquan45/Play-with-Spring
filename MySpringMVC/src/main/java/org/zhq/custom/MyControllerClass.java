package org.zhq.custom;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MyController
@MyRequestMapping("/demo")
public class MyControllerClass {
    @MyAutowired
    private MyServiceClass serviceClass;

    @MyRequestMapping("/query")
    public void query(HttpServletResponse response, @MyRequestParam("name") String name) {
        try {
            response.getWriter().write(serviceClass.get(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @MyRequestMapping("/add")
    public void add(@MyRequestParam("a")Integer a,@MyRequestParam("b")Integer b,HttpServletResponse response){
        try {
            response.getWriter().write(a+"+"+b+"="+(a+b));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @MyRequestMapping("/post")
    public void post(@MyRequestBody Person person, HttpServletResponse response){
        try {
            response.getWriter().write(person.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @MyRequestMapping("/hello")
    public ModelAndView index(@MyRequestParam("name") String name,
                              HttpServletRequest request,
                              HttpServletResponse response
    ) {
        ModelAndView mv = new ModelAndView();
        mv.getModelMap().put("greeting", serviceClass.sayHello(name));
        mv.getModelMap().put("name", name);
        mv.setViewName("index");
        return mv;
    }

    @MyRequestMapping("/hello1")
    @MyResponseBody
    public String hello1(@MyRequestParam("name") String name,
                         HttpServletRequest request,
                         HttpServletResponse response
    ) {
        return serviceClass.sayHello(name);
    }


}