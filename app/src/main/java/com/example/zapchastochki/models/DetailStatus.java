package com.example.zapchastochki.models;

import java.util.Arrays;

public enum DetailStatus {
    NOT_IN_STACK, ARRIVED, ORDERED, DENIED;

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
}
