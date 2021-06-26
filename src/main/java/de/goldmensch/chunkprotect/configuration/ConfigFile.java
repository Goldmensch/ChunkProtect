package de.goldmensch.chunkprotect.configuration;

import de.goldmensch.chunkprotect.configuration.elements.Localization;

public class ConfigFile {
    private int version = 1;
    private Localization localization = new Localization();

    public int getVersion() {
        return version;
    }

    public Localization getLocalization() {
        return localization;
    }
}
