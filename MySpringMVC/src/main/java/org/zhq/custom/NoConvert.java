package org.zhq.custom;

public class NoConvert implements Converter {
    @Override
    public Object doConvert(String parameter) {
        return parameter;
    }
}
