package de.goldmensch.chunkprotect.configuration.entities;

import de.goldmensch.chunkprotect.configuration.protection.elements.options.ChunkOption;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class EntitiesConfiguration {
    private final EntityProtection defaultProtection;
    private int version = 1;
    private final Map<EntityType, EntityProtection> protectionMap = new HashMap<>();
    private final Path path;

    public EntitiesConfiguration(Path path, EntityProtection defaultProtection) {
        this.path = path;
        this.defaultProtection = defaultProtection;
    }

    public void init() throws IOException {
        build();
        reload();
    }

    private void build() throws IOException {
        if(Files.notExists(path)) {
            Files.createFile(path);
            Files.writeString(path,
                    "# Schema: TYPE: damageInClaimed, playerInteractInClaimed, damageInUnclaimed, playerInteractInUnclaimed"
                    + System.lineSeparator() + "# true=protected; false=not protected"
                    + System.lineSeparator() + "# Example: SHEEP: true, false, false, false"
                    + System.lineSeparator() + "# Entity Types(TYPE): https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html"
                    + System.lineSeparator()+ "version: " + version);
        }
        version = getVersionFromFile();
    }

    private int getVersionFromFile() throws IOException {
        for(String line : Files.readAllLines(path)) {
            String[] data = line.split(":");
            if(data[0].equalsIgnoreCase("version")) return Integer.parseInt(data[1].trim());
        }
        return 1;
    }

    public void reload() throws IOException {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(Files.newBufferedReader(path));
        for(String key : config.getKeys(false)) {
            if(key.contains("version")) continue;
            String[] rawValues = config.getString(key).split(",");
            EntityType type = EntityType.valueOf(key.toUpperCase());
            if(rawValues.length != 4) continue;

            boolean[] values = new boolean[4];
            for(int i = 0; i < 4; i++) {
                values[i] = Boolean.parseBoolean(rawValues[i]);
            }

            protectionMap.put(type,
                    new EntityProtection(new ChunkOption(values[0], values[2]),
                            new ChunkOption(values[1], values[3])));
        }
    }

    public EntityProtection getProtection(EntityType type) {
        EntityProtection protection = protectionMap.get(type);
        return protection == null ? defaultProtection : protection;
    }

    public int getVersion() {
        return version;
    }
}
