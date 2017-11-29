package com.marcosevaristo.tcc001.utils;


import java.util.Map;

public class MapUtils {

    public static boolean isEmpty(Map<?,?> map) {
         return map == null || map.size() == 0;
    }

    public static boolean isNotEmpty(Map<?,?> map) {
        return !isEmpty(map);
    }

}
