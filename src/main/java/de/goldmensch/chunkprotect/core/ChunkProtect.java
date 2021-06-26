package de.goldmensch.chunkprotect.core;

import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.configuration.ConfigFile;
import de.goldmensch.chunkprotect.configuration.Configuration;
import de.goldmensch.chunkprotect.listener.ProtectListeners;
import de.goldmensch.chunkprotect.listener.PlayerJoinListener;
import de.goldmensch.chunkprotect.message.Messenger;
import de.goldmensch.chunkprotect.storage.StorageType;
import de.goldmensch.chunkprotect.storage.services.DataService;
import de.goldmensch.chunkprotect.utils.PermissionUtil;
import de.goldmensch.chunkprotect.utils.SafeExceptions;
import de.goldmensch.smartutils.plugin.SmartPlugin;

public class ChunkProtect extends SmartPlugin {
    private DataService dataService;
    private Messenger messenger;
    private ConfigFile config;
    private Configuration configuration;

    @Override
    public void onLoad() {
        initConfig();
        initData();
        initMessenger();
    }

    @Override
    public void onEnable() {
        if(!getServer().getPluginManager().isPluginEnabled(this)) return;
        registerListener(new PlayerJoinListener(this), new ProtectListeners(this));
        registerCommand("chunkprotect", new ChunkProtectCommand(this));
    }

    @Override
    public void onDisable() {
        invalidateCached();
    }

    private void invalidateCached() {
        dataService.getCache().getChunkCache().invalidateAll();
        dataService.getCache().getHolderCache().invalidateAll();
    }

    private void initData() {
        dataService = SafeExceptions.safeIOException(this,
                () -> DataService.loadService(getDataFolder().toPath().resolve("data"), StorageType.JSON));
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
}
