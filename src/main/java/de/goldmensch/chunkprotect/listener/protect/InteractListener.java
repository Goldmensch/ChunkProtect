package de.goldmensch.chunkprotect.listener.protect;

import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.storage.services.DataService;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener extends EntityListeners{
    public InteractListener(DataService dataService, ChunkProtect chunkProtect) {
        super(dataService, chunkProtect);
    }

    @EventHandler
    public void handleBucketFill(PlayerBucketFillEvent event) {
        if(forbidden(event.getPlayer(), event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleBucketEmpty(PlayerBucketEmptyEvent event) {
        if(forbidden(event.getPlayer(), event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handlePlayerInteractEntity(PlayerInteractEntityEvent event) {
        if(forbidden(event.getPlayer(), event.getRightClicked().getChunk())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void openInventoryEvent(InventoryOpenEvent event) {
        Location location = lastClicked.get(event.getPlayer().getUniqueId());
        if(location != null) {
            if(forbidden(event.getPlayer(), location.getChunk())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void handlePlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(event.getInteractionPoint() == null) return;
            lastClicked.put(event.getPlayer().getUniqueId(), event.getInteractionPoint());
            Block block = event.getClickedBlock();
            if(block != null && block.getType() == Material.JUKEBOX) {
                if(forbidden(event.getPlayer(), block.getChunk())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
