package de.goldmensch.chunkprotect.listener.protect;

import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.util.ChunkUtil;
import de.goldmensch.chunkprotect.storage.services.DataService;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

    @EventHandler
    public void handleHangingDestroy(HangingBreakByEntityEvent event) {
        unwrapPlayer(event.getRemover()).ifPresent(player -> {
            if(forbidden(player, event.getEntity().getChunk())) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void handleHangingPlace(HangingPlaceEvent event) {
        if(forbidden(event.getPlayer(), event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Chunk chunk = event.getEntity().getChunk();
        if (damager instanceof Player) {
            if(forbidden((Player) damager, chunk)) {
                event.setCancelled(true);
            }
        }else {
            if(ChunkUtil.getChunk(chunk, dataService).isClaimed()) event.setCancelled(true);
        }
    }

    /*
    Explosion protection
     */
    @EventHandler
    public void handleEntityExplosion(EntityExplodeEvent event) {
        onExplodeEvent(event.blockList());
    }

    @EventHandler
    public void handleBlockExplode(BlockExplodeEvent event) {
        onExplodeEvent(event.blockList());
    }

    @EventHandler
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
