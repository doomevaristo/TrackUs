package com.marcosevaristo.tcc001.utils;

public class StringUtils {

    public static boolean isBlank(String texto) {
        return texto == null || texto.trim() == "";
    }

    public static boolean isNotBlank(String texto) {
        return !isBlank(texto);
    }

}
