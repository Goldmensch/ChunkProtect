package de.goldmensch.chunkprotect.core;

import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.configuration.entities.EntitiesConfiguration;
import de.goldmensch.chunkprotect.configuration.entities.EntityProtection;
import de.goldmensch.chunkprotect.configuration.plugin.ConfigFile;
import de.goldmensch.chunkprotect.configuration.plugin.PluginConfig;
import de.goldmensch.chunkprotect.configuration.protection.ProtectionConfig;
import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.listener.ChunkLoadListener;
import de.goldmensch.chunkprotect.listener.PlayerJoinQuitListener;
import de.goldmensch.chunkprotect.listener.protect.ProtectListeners;
import de.goldmensch.chunkprotect.message.Messenger;
import de.goldmensch.chunkprotect.storage.services.DataService;
import de.goldmensch.chunkprotect.utils.SafeExceptions;
import de.goldmensch.chunkprotect.utils.Util;
import de.goldmensch.smartutils.plugin.SmartPlugin;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChunkProtect extends SmartPlugin {
    private final ProtectionBypass protectionBypass = new ProtectionBypass();
    private DataService dataService;
    private Messenger messenger;
    private PluginConfig pluginConfig;
    private EntitiesConfiguration entitiesConfiguration;
    private ProtectionConfig protectionConfig;
    private ExecutorService service;

    @Override
    public void onLoad() {
        service = Executors.newFixedThreadPool(3);
    }

    @Override
    public void onEnable() {
        initConfig();
        initMessenger();
        initData();

        registerListener(new PlayerJoinQuitListener(this),
                new ProtectListeners(this),
                new ChunkLoadListener(this));

        registerCommand("chunkprotect", new ChunkProtectCommand(this));
    }

    @Override
    public void onDisable() {
        dataService.saveALl(true);
        service.shutdown();
    }

    private void initData() {
        dataService = SafeExceptions.safeIOException(this,
                () -> DataService.loadService(this));

        getServer().getWorlds().forEach(world ->
                Arrays.stream(world.getLoadedChunks()).forEach(chunk ->
                        dataService.loadChunkIfUnloaded(ChunkLocation.fromChunk(chunk))));
    }

    private void initConfig() {
        Path pluginDir = getDataFolder().toPath();
        Path protectionDir = pluginDir.resolve("protection");
        pluginConfig = new PluginConfig(getDataFolder().toPath().resolve("config.yml"));
        SafeExceptions.safeIOException(this, () -> pluginConfig.init());

        protectionConfig = new ProtectionConfig(protectionDir.resolve("protection.yml"));
        SafeExceptions.safeIOException(this, () -> protectionConfig.init());

        entitiesConfiguration = new EntitiesConfiguration(protectionDir.resolve("entities.yml"),
                new EntityProtection(getProtectionConfig().getConfigFile().getEntity().getDamage(),
                        getProtectionConfig().getConfigFile().getEntity().getPlayerInteract()));
        SafeExceptions.safeIOException(this, () -> entitiesConfiguration.init());
    }

    private void initMessenger() {
        messenger = SafeExceptions.safeIOException(this, () -> Messenger.setupMessenger(this,
                getConfigFile().getLocalization().isMiniMessage(), getConfigFile().getLocalization().isUseActionBar()));
    }

    public DataService getDataService() {
        return dataService;
    }

    public String getPermission(String... args) {
        return Util.build("chunkprotect", args);
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public ExecutorService getService() {
        return service;
    }

    public EntitiesConfiguration getEntitiesConfiguration() {
        return entitiesConfiguration;
    }

    public ProtectionBypass getProtectionBypass() {
        return protectionBypass;
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    public ConfigFile getConfigFile() {
        return pluginConfig.getConfigFile();
    }

    public ProtectionConfig getProtectionConfig() {
        return protectionConfig;
    }
}
