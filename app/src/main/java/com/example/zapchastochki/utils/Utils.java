package com.example.zapchastochki.utils;

import java.util.Arrays;

public class Utils {
    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
    public static Settings settings;
    //public static String serverAddress = "http://192.168.1.2:54436";
    public static String serverAddress = "https://cbook.belcraft.ru";
}
