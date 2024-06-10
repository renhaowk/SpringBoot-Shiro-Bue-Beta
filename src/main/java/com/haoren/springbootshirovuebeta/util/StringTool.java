package com.haoren.springbootshirovuebeta.util;

public class StringTool {
    public static boolean isNullOrEmpty(String str) {
        return null == str || "".equals(str) || "null".equals(str);
    }

    public static boolean isNullOrEmpty(Object obj){
        return null == obj || "".equals(obj);
    }
}
