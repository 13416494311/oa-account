package com.ruoyi.common.utils;

public class NumberUtils {

    public static boolean isNumber(String str){
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }
}
