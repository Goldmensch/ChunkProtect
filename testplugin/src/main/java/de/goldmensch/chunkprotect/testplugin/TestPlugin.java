package de.goldmensch.chunkprotect.testplugin;

import de.goldmensch.chunkprotect.ChunkProtect;
import de.goldmensch.chunkprotect.events.ChunkClaimEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin implements Listener {
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
        getServer().getPluginManager().registerEvents(this, this);
    }

    public ChunkProtect getChunkProtect() {
        return chunkProtect;
    }

    @EventHandler
    public void handleClaim(ChunkClaimEvent event) {
        event.setCancelled(true);
    }
}
