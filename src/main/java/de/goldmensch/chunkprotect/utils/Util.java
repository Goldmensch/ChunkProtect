package de.goldmensch.chunkprotect.utils;

import java.util.function.Consumer;

public class Util {

    public static <T> T executeAndReturn(T t, Consumer<T> function) {
        function.accept(t);
        return t;
    }
}
