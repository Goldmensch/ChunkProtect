package de.goldmensch.chunkprotect.configuration.entities;

import de.goldmensch.chunkprotect.configuration.protection.elements.options.ChunkOption;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public class EntitiesConfiguration {
    private final EntityProtection defaultProtection;
    private final Map<EntityType, EntityProtection> protectionMap = new EnumMap<>(EntityType.class);
    private final Path path;
    private int version = 1;

    public EntitiesConfiguration(Path path, EntityProtection defaultProtection) {
        this.path = path;
        this.defaultProtection = defaultProtection;
    }

    public void init() throws IOException {
        build();
        reload();
    }

    private void build() throws IOException {
        if (Files.notExists(path)) {
            Files.createFile(path);
            Files.writeString(path,
                    "# Schema: TYPE: damageInClaimed, playerInteractInClaimed, damageInUnclaimed, playerInteractInUnclaimed"
                            + System.lineSeparator() + "# true=protected; false=not protected"
                            + System.lineSeparator() + "# Example: SHEEP: true, false, false, false"
                            + System.lineSeparator() + "# Entity Types(TYPE): https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html"
                            + System.lineSeparator() + "version: " + version);
        }
        version = getVersionFromFile();
    }

    private int getVersionFromFile() throws IOException {
        for (String line : Files.readAllLines(path)) {
            String[] data = line.split(":");
            if ("version".equalsIgnoreCase(data[0])) return Integer.parseInt(data[1].trim());
        }
        return 1;
    }

    public void reload() throws IOException {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(Files.newBufferedReader(path));
        for (Map.Entry<String, Object> entry : config.getValues(false).entrySet()) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            String[] rawValues = value.split(",");
            if (rawValues.length != 4 || "version".contains(key)) continue;
            EntityType type = EntityType.valueOf(key.toUpperCase());

            boolean[] values = new boolean[4];
            for (int i = 0; i < 4; i++) {
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
