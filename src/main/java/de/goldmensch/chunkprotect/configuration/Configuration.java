package de.goldmensch.chunkprotect.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.lang.invoke.TypeDescriptor;
import java.nio.file.Files;
import java.nio.file.Path;

public class Configuration<T> {
    private final Path path;
    private T configValues;
    private final ObjectMapper mapper;

    public Configuration(Path path, T defaultFile) {
        this.path = path;
        this.mapper = new ObjectMapper(new YAMLFactory());
        this.configValues = defaultFile;
    }

    public void init() throws IOException {
        initMapper();

        buildOfNotExist();
        reload();
    }

    private void initMapper() {
        mapper.findAndRegisterModules();
    }

    private void buildOfNotExist() throws IOException {
        if(Files.notExists(path)) {
            Files.createDirectories(path.getParent());
            mapper.writeValue(Files.newBufferedWriter(path), configValues);
        }
    }

    public void reload() throws IOException {
        configValues = mapper.readValue(path.toFile(), mapper.constructType(configValues.getClass()));
    }

    public T getConfigFile() {
        return configValues;
    }
}
