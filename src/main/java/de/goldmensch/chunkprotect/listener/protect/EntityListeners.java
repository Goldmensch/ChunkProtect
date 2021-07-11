package de.goldmensch.chunkprotect.listener.protect;

import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.utils.ChunkUtil;
import de.goldmensch.chunkprotect.storage.services.DataService;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityListeners extends BlockListeners{
    public EntityListeners(DataService dataService, ChunkProtect chunkProtect) {
        super(dataService, chunkProtect);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleHangingDestroy(HangingBreakByEntityEvent event) {
        unwrapPlayer(event.getRemover()).ifPresent(player -> {
            if(forbidden(player, event.getEntity().getChunk())) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleHangingPlace(HangingPlaceEvent event) {
        if(event.getPlayer() == null)return;
        if(forbidden(event.getPlayer(), event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!entitiesConfiguration.getProtection(event.getEntityType()).damage()) return;
        Chunk chunk = event.getEntity().getChunk();

        unwrapPlayer(event.getDamager()).ifPresentOrElse(player -> {
            if(forbidden(player, chunk)) {
                event.setCancelled(true);
            }
        }, () -> {
            if(ChunkUtil.getChunk(chunk, dataService).isClaimed()) event.setCancelled(true);
        });
    }

    /*
    Explosion protection
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void handleEntityExplosion(EntityExplodeEvent event) {
        onExplodeEvent(event.blockList());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBlockExplode(BlockExplodeEvent event) {
        onExplodeEvent(event.blockList());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleTntPrime(TNTPrimeEvent event) {
        unwrapPlayer(event.getPrimerEntity()).ifPresent(player -> {
            if(forbidden(player, event.getBlock().getChunk())) {
                event.setCancelled(true);
            }
        });
    }

    private void onExplodeEvent(List<Block> blockList) {
        Set<Block> protectedBlocks = new HashSet<>();
        for(Block block : blockList) {
            ChunkUtil.getChunk(block.getChunk(), dataService).ifClaimed(claimedChunk ->
                    protectedBlocks.add(block));
        }
        blockList.removeAll(protectedBlocks);
    }
}
