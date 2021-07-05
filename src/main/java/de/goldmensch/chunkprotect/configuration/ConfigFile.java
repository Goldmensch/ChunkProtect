package de.goldmensch.chunkprotect.configuration;

import de.goldmensch.chunkprotect.configuration.elements.Localization;
import de.goldmensch.chunkprotect.configuration.elements.Storage;

public class ConfigFile {
    private int version = 1;
    private Localization localization = new Localization();
    private Storage storage = new Storage();

    public int getVersion() {
        return version;
    }

    public Localization getLocalization() {
        return localization;
    }

    public Storage getStorage() {
        return storage;
    }
}
