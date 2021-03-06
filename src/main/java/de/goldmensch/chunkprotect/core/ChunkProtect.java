package de.goldmensch.chunkprotect.core;

import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.configuration.ConfigFile;
import de.goldmensch.chunkprotect.configuration.Configuration;
import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.listener.ChunkLoadListener;
import de.goldmensch.chunkprotect.listener.ProtectListeners;
import de.goldmensch.chunkprotect.listener.PlayerJoinQuitListener;
import de.goldmensch.chunkprotect.message.Messenger;
import de.goldmensch.chunkprotect.storage.StorageType;
import de.goldmensch.chunkprotect.storage.services.DataService;
import de.goldmensch.chunkprotect.utils.PermissionUtil;
import de.goldmensch.chunkprotect.utils.SafeExceptions;
import de.goldmensch.smartutils.plugin.SmartPlugin;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChunkProtect extends SmartPlugin {
    private DataService dataService;
    private Messenger messenger;
    private ConfigFile config;
    private Configuration configuration;

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
        dataService.safeAll();
    }

    private void initData() {
        dataService = SafeExceptions.safeIOException(this,
                () -> DataService.loadService(this));

        getServer().getWorlds().forEach(world ->
                Arrays.stream(world.getLoadedChunks()).forEach(chunk ->
                        dataService.loadChunk(ChunkLocation.fromChunk(chunk))));
    }

    private void initConfig() {
        configuration = new Configuration(getDataFolder().toPath().resolve("config.yml"));
        SafeExceptions.safeIOException(this, () -> configuration.init());
        config = configuration.getConfigFile();
    }

    private void initMessenger() {
        messenger = SafeExceptions.safeIOException(this, () -> Messenger.setupMessenger(this,
                config.getLocalization().isMiniMessage(), config.getLocalization().isUseActionBar()));
    }

    public DataService getDataService() {
        return dataService;
    }

    public String getPermission(String... args) {
        return PermissionUtil.build("chunkprotect", args);
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public ExecutorService getService() {
        return service;
    }
}
