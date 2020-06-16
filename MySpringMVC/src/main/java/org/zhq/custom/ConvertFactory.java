package org.zhq.custom;

public class ConvertFactory {
    public static Converter getConvert(Class<?> klass){
        if(Integer.class.equals(klass)||int.class.equals(klass)){
            return new IntegerConverter();
        }
        return new NoConvert();
    }
}
