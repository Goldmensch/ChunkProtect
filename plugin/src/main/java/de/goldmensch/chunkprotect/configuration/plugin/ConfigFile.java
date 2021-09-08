package de.goldmensch.chunkprotect.configuration.plugin;

import de.goldmensch.chunkprotect.configuration.plugin.elements.Localization;
import de.goldmensch.chunkprotect.configuration.plugin.elements.Notification;
import de.goldmensch.chunkprotect.configuration.plugin.elements.Storage;

public class ConfigFile {
    private int version = 1;
    private Localization localization = new Localization();
    private Storage storage = new Storage();
    private Notification notification = new Notification();

    public int getVersion() {
        return version;
    }

    public Localization getLocalization() {
        return localization;
    }

    public Storage getStorage() {
        return storage;
    }

    public Notification getNotification() {
        return notification;
    }
}
