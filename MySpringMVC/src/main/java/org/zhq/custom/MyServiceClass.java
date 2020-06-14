package org.zhq.custom;

@MyService
public class MyServiceClass {
    public String sayHello(String name) {
        return "Hello " + name;
    }

    public String get(String name) {
        return "My name is " + name;
    }
}