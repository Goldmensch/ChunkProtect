package de.goldmensch.chunkprotect.listener;

import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.storage.services.DataService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.ExecutorService;

public class PlayerJoinQuitListener implements Listener {

    private DataService dataService;
    private ExecutorService executorService;

    public PlayerJoinQuitListener(ChunkProtect protect) {
        this.dataService = protect.getDataService();
        this.executorService = protect.getService();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleJoin(AsyncPlayerPreLoginEvent event) {
        if(event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            executorService.execute(() ->
                    dataService.setupHolder(event.getUniqueId(), event.getName(), true));
        }
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        executorService.execute(() ->
                dataService.saveHolder(event.getPlayer().getUniqueId()));
    }
}
