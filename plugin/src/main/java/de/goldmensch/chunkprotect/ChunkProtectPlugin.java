package de.goldmensch.chunkprotect;

import de.goldmensch.chunkprotect.api.ApiChunkProtect;
import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.configuration.entities.EntitiesConfiguration;
import de.goldmensch.chunkprotect.configuration.entities.EntityProtection;
import de.goldmensch.chunkprotect.configuration.plugin.ConfigFile;
import de.goldmensch.chunkprotect.configuration.plugin.PluginConfig;
import de.goldmensch.chunkprotect.configuration.protection.ProtectionConfig;
import de.goldmensch.chunkprotect.listener.ChunkLoadListener;
import de.goldmensch.chunkprotect.listener.PlayerJoinQuitListener;
import de.goldmensch.chunkprotect.listener.PlayerMoveListener;
import de.goldmensch.chunkprotect.listener.protect.ProtectListeners;
import de.goldmensch.chunkprotect.message.IMessenger;
import de.goldmensch.chunkprotect.message.Messenger;
import de.goldmensch.chunkprotect.storage.services.DataService;
import de.goldmensch.smartutils.plugin.SmartPlugin;
import org.bukkit.plugin.ServicePriority;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChunkProtectPlugin extends SmartPlugin {
    private final ChunkProtectionBypass protectionBypass = new ChunkProtectionBypass();
    private DataService dataService;
    private IMessenger messenger;
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
                new ChunkLoadListener(this),
                new PlayerMoveListener(this));

        registerCommand("chunkprotect", new ChunkProtectCommand(this));

        getServer().getServicesManager().register(ChunkProtect.class, new ApiChunkProtect(this), this, ServicePriority.High);
    }

    @Override
    public void onDisable() {
        dataService.saveAll(true);
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
        StringBuilder builder = new StringBuilder("chunkprotect.");
        for (String c : args) {
            builder.append(c);
            builder.append(".");
        }
        return builder.substring(0, builder.length() - 1);
    }

    public IMessenger getMessenger() {
        return messenger;
    }

    public ExecutorService getService() {
        return service;
    }

    public EntitiesConfiguration getEntitiesConfiguration() {
        return entitiesConfiguration;
    }

    public ChunkProtectionBypass getProtectionBypass() {
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
