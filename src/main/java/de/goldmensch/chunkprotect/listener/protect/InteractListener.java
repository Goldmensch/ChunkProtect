package de.goldmensch.chunkprotect.listener.protect;

import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.storage.services.DataService;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

public class InteractListener extends EntityListeners{
    public InteractListener(DataService dataService, ChunkProtect chunkProtect) {
        super(dataService, chunkProtect);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBucketFill(PlayerBucketFillEvent event) {
        if(forbidden(event.getPlayer(), event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBucketEmpty(PlayerBucketEmptyEvent event) {
        if(forbidden(event.getPlayer(), event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handlePlayerInteractEntity(PlayerInteractEntityEvent event) {
        if(!entitiesConfiguration.getProtection(event.getRightClicked().getType()).interact()) return;
        if(forbidden(event.getPlayer(), event.getRightClicked().getChunk())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handlePlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null
                && event.getClickedBlock().getType() != Material.AIR
                && event.useInteractedBlock() == Event.Result.ALLOW
                && (((event.getAction() == Action.RIGHT_CLICK_BLOCK
                && (!event.isBlockInHand() || !event.getPlayer().isSneaking())
                && event.useInteractedBlock() == Event.Result.ALLOW)
                && event.getClickedBlock().getType() != Material.ENDER_CHEST)
                || event.getAction() == Action.PHYSICAL)) {
            if(forbidden(event.getPlayer(), event.getClickedBlock().getChunk())) {
                event.setUseInteractedBlock(Event.Result.DENY);
            }
        }
    }
}
