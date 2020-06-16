package org.zhq.custom;

public class IntegerConverter implements Converter{
    @Override
    public Object doConvert(String parameter) {
        return Integer.valueOf(parameter);
    }
}
