package de.goldmensch.chunkprotect;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Resources {
    private Resources() {
    }

    public static void copyResource(String from, Path to) throws IOException {
        try (InputStream in = Resources.class.getResourceAsStream("/" + from)) {
            Files.copy(in, to);
        }
    }
}