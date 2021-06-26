package de.goldmensch.chunkprotect.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    public static void copyResource(String from, Path to) throws IOException {
        try(InputStream in = FileUtils.class.getResourceAsStream("/" + from)) {
            Files.copy(in, to);
        }
    }

}
