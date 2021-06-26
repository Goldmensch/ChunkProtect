package de.goldmensch.chunkprotect.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

public class JsonUtil {

    public static  <T> T getOrDefault(JsonObject object, Function<JsonObject, T> function, T defaultValue) {
        T value = function.apply(object);
        if(value != null) {
            return value;
        }else {
            return defaultValue;
        }
    }

    public static void write(Object object, Gson gson, Path path) {
        try(BufferedWriter writer = Files.newBufferedWriter(path)) {
            gson.toJson(object, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T read(Class<T> t, Gson gson, Path path) {
        try(BufferedReader reader = Files.newBufferedReader(path)) {
            return gson.fromJson(reader, t);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean createNewJsonFile(Path path) {
        try {
            if(Files.notExists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                Files.writeString(path, "[]");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
