package de.goldmensch.chunkprotect.listener;

import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.storage.services.DataService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private DataService dataService;

    public PlayerJoinListener(ChunkProtect protect) {
        this.dataService = protect.getDataService();
    }

    @EventHandler
    public void handle(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        dataService.createOrUpdate(player.getUniqueId(), player.getName(), true);
    }

}
