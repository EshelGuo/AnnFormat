package com.eshel.core.util;

import java.util.Collection;
import java.util.Map;

/**
 * Created by EshelGuo on 2019/6/14.
 * 工具方法集中于此
 */
public class Util {

    public static boolean isEmpty(Collection list){
        return list == null || list.isEmpty();
    }

    public static boolean notEmpty(Collection list){
        return !isEmpty(list);
    }

    public static boolean isEmpty(CharSequence text){
        return text == null || text.length() == 0;
    }

    public static boolean notEmpty(CharSequence text){
        return !isEmpty(text);
    }

    public static boolean isNull(Object object){
        return object == null;
    }

    public static boolean notNull(Object object){
        return !isNull(object);
    }

    public static boolean notEmpty(Map map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(Map map){
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean notEmpty(Object[] array) {
        return !isEmpty(array);
    }

    public static Integer toInteger(String source){
        try {
            return Integer.valueOf(source);
        }catch (Exception e){
            // do nothing
        }
        return null;
    }

    //获取数组 array 中 object 的角标
    public static int indexOf(Object[] array, Object object){
        if(array == null)
            return -1;
        for (int i = 0; i < array.length; i++) {
            if(array[i] == object)
                return i;
        }
        return -1;
    }
}
