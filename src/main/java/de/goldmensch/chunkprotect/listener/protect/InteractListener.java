package de.goldmensch.chunkprotect.listener.protect;

import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.storage.services.DataService;
import de.goldmensch.chunkprotect.utils.ChunkUtil;
import de.goldmensch.chunkprotect.utils.EventUtil;
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
        ChunkUtil.getChunk(event.getBlock().getChunk(), dataService).ifClaimedOr(chunk -> {
            if(protectionFile.getOther().getBucketFill().isClaimed()) {
                if(forbidden(event.getPlayer(), chunk)) event.setCancelled(true);
            }
        }, () -> {
            if(protectionFile.getOther().getBucketFill().isUnclaimed()) {
                sendYouCantDoThat(event.getPlayer());
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBucketEmpty(PlayerBucketEmptyEvent event) {
        ChunkUtil.getChunk(event.getBlock().getChunk(), dataService).ifClaimedOr(chunk -> {
            System.out.println(protectionFile.getOther().getBucketEmpty().isClaimed());
            if(protectionFile.getOther().getBucketEmpty().isClaimed()) {
                if(forbidden(event.getPlayer(), chunk)) event.setCancelled(true);
            }
        }, () -> {
            if(protectionFile.getOther().getBucketEmpty().isUnclaimed()) {
                sendYouCantDoThat(event.getPlayer());
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handlePlayerInteractEntity(PlayerInteractEntityEvent event) {
        ChunkUtil.getChunk(event.getRightClicked().getChunk(), dataService).ifClaimedOr(chunk -> {
            if(protectionFile.getEntity().getPlayerInteract().isClaimed()) {
                if(forbidden(event.getPlayer(), chunk)) event.setCancelled(true);
            }
        }, () -> {
            if(protectionFile.getEntity().getPlayerInteract().isUnclaimed()) sendYouCantDoThat(event.getPlayer());
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handlePlayerInteract(PlayerInteractEvent event) {
        System.out.println(event.getEventName());
        if (event.getClickedBlock() != null
                && event.getClickedBlock().getType() != Material.AIR
                && event.useInteractedBlock() == Event.Result.ALLOW
                && (((event.getAction() == Action.RIGHT_CLICK_BLOCK
                && (!event.isBlockInHand() || !event.getPlayer().isSneaking())
                && event.useInteractedBlock() == Event.Result.ALLOW)
                && event.getClickedBlock().getType() != Material.ENDER_CHEST)
                && !EventUtil.isBucketEvent(event)
                || event.getAction() == Action.PHYSICAL)) {

            ChunkUtil.getChunk(event.getClickedBlock().getChunk(), dataService).ifClaimed(chunk -> {
                if(forbidden(event.getPlayer(), chunk)) event.setCancelled(true);
            });
        }
    }
}
