package de.goldmensch.chunkprotect.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Configuration {
    private final Path path;
    private ConfigFile configFile;
    private final ObjectMapper mapper;

    public Configuration(Path path) {
        this.path = path;
        this.mapper = new ObjectMapper(new YAMLFactory());
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
            configFile = new ConfigFile();
            mapper.writeValue(Files.newBufferedWriter(path), configFile);
        }
    }

    public void reload() throws IOException {
        configFile = mapper.readValue(path.toFile(), ConfigFile.class);
    }

    public ConfigFile getConfigFile() {
        return configFile;
    }
}
