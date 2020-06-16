package org.zhq.custom;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
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


}