package de.goldmensch.chunkprotect.listener;

import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.storage.services.DataService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.ExecutorService;

public class PlayerJoinQuitListener implements Listener {

    private final DataService dataService;
    private final ExecutorService executorService;
    private final ChunkProtectPlugin protect;

    public PlayerJoinQuitListener(ChunkProtectPlugin protect) {
        this.dataService = protect.getDataService();
        this.executorService = protect.getService();
        this.protect = protect;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleJoin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            dataService.setupHolder(event.getUniqueId(), event.getName(), true);
        }
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        protect.getProtectionBypass().remove(event.getPlayer().getUniqueId());
        executorService.execute(() ->
                dataService.saveHolder(event.getPlayer().getUniqueId()));
    }
}
