package de.goldmensch.chunkprotect.testplugin;

import de.goldmensch.chunkprotect.ChunkProtect;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin{
    private ChunkProtect chunkProtect;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<ChunkProtect> chunkProtectProvider = getServer().getServicesManager().getRegistration(ChunkProtect.class);

        if(chunkProtectProvider == null) {
            getLogger().severe("API not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        chunkProtect = chunkProtectProvider.getProvider();

        getCommand("testChunk").setExecutor(new TestCommand(this));
    }

    public ChunkProtect getChunkProtect() {
        return chunkProtect;
    }
}
