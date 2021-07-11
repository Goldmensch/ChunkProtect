package de.goldmensch.chunkprotect.configuration.entities;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class EntitiesConfiguration {
    private final Map<EntityType, EntityProtection> protectionMap = new HashMap<>();
    private final Path path;
    private final boolean defaultProtection;

    public EntitiesConfiguration(Path path, boolean defaultProtection) {
        this.path = path;
        this.defaultProtection = defaultProtection;
    }

    public void init() throws IOException {
        Files.createDirectories(path.getParent());
        if(Files.notExists(path)) {
            Files.createFile(path);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(Files.newBufferedReader(path));
        for(String key : config.getKeys(false)) {
            String[] values = config.getString(key).split(";");
            if(values.length != 2) continue;
            protectionMap.put(EntityType.valueOf(key.toUpperCase()),
                    new EntityProtection(Boolean.parseBoolean(values[1].trim()), Boolean.parseBoolean(values[0].trim())));
        }
    }

    public EntityProtection getProtection(EntityType type) {
        EntityProtection protection = protectionMap.get(type);
        if(protection == null) {
            protection = new EntityProtection(defaultProtection, defaultProtection);
            protectionMap.put(type, protection);
        }

        return protection;
    }

}
