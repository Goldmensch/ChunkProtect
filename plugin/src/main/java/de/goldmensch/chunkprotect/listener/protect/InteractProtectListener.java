package de.goldmensch.chunkprotect.listener.protect;

import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.storage.services.DataService;
import de.goldmensch.chunkprotect.Chunks;
import de.goldmensch.chunkprotect.listener.Events;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractProtectListener extends EntityListeners {
    public InteractProtectListener(DataService dataService, ChunkProtectPlugin chunkProtectPlugin) {
        super(dataService, chunkProtectPlugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBucketFill(PlayerBucketFillEvent event) {
        Chunks.getChunk(event.getBlock().getChunk(), dataService).ifClaimedOr(chunk -> {
            if (protectionFile.getOther().getBucketFill().isClaimed() && forbidden(event.getPlayer(), chunk)) {
                event.setCancelled(true);
            }
        }, () -> {
            if (protectionFile.getOther().getBucketFill().isUnclaimed() && hasNoBypass(event.getPlayer())) {
                sendYouCantDoThat(event.getPlayer());
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBucketEmpty(PlayerBucketEmptyEvent event) {
        Chunks.getChunk(event.getBlock().getChunk(), dataService).ifClaimedOr(chunk -> {
            if (protectionFile.getOther().getBucketEmpty().isClaimed() && forbidden(event.getPlayer(), chunk)) {
                event.setCancelled(true);
            }
        }, () -> {
            if (protectionFile.getOther().getBucketEmpty().isUnclaimed() && hasNoBypass(event.getPlayer())) {
                sendYouCantDoThat(event.getPlayer());
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handlePlayerInteractEntity(PlayerInteractEntityEvent event) {
        Chunks.getChunk(event.getRightClicked().getChunk(), dataService).ifClaimedOr(chunk -> {
            if (entitiesConfiguration.getProtection(event.getRightClicked().getType()).getPlayerInteract().isClaimed() && forbidden(event.getPlayer(), chunk)) {
                event.setCancelled(true);
            }
        }, () -> {
            if (entitiesConfiguration.getProtection(event.getRightClicked().getType()).getPlayerInteract().isUnclaimed()
                    && hasNoBypass(event.getPlayer())) {
                sendYouCantDoThat(event.getPlayer());
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handlePlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null
                && event.getClickedBlock().getType() != Material.AIR
                && event.useInteractedBlock() == Event.Result.ALLOW
                && (((event.getAction() == Action.RIGHT_CLICK_BLOCK
                    && (!event.isBlockInHand() || !event.getPlayer().isSneaking())
                    && event.getClickedBlock().getType().isInteractable()
                    && event.useInteractedBlock() == Event.Result.ALLOW)
                && event.getClickedBlock().getType() != Material.ENDER_CHEST)
                && !Events.isBucketEvent(event)
                || event.getAction() == Action.PHYSICAL)) {

            if(handleBlock(protectionFile.getBlock().getPlayerInteract(), event.getPlayer(), event.getClickedBlock().getChunk())) {
                event.setCancelled(true);
            }
        }
    }
}
