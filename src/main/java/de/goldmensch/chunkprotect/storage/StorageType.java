package de.goldmensch.chunkprotect.storage;

public enum StorageType {
    JSON("JSON");

    private final String name;

    StorageType(String name) {
        this.name = name;
    }

    public static StorageType getByName(String name) {
        for(StorageType c : StorageType.values()) {
            if(name.equalsIgnoreCase(c.name)) return c;
        }
        return null;
    }
}
