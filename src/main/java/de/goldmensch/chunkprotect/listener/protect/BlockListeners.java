package de.goldmensch.chunkprotect.listener.protect;

import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.util.ChunkUtil;
import de.goldmensch.chunkprotect.storage.services.DataService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;


public class BlockListeners extends ListenerData {
    public BlockListeners(DataService dataService, ChunkProtect chunkProtect) {
        super(dataService, chunkProtect);
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        if(forbidden(event.getPlayer(), event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        if(forbidden(event.getPlayer(), event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleBlockTo(BlockFromToEvent event) {
        if(event.getToBlock().getChunk() != event.getBlock().getChunk()) {
            ClaimableChunk from = ChunkUtil.getChunk(event.getBlock().getChunk(), dataService);
            ClaimableChunk to = ChunkUtil.getChunk(event.getToBlock().getChunk(), dataService);
            if(checkFromTo(from, to)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void handlePistonBlockExtend(BlockPistonExtendEvent event) {
        ClaimableChunk pistonChunk = ChunkUtil.getChunk(event.getBlock().getChunk(), dataService);
        event.getBlocks().forEach(block -> {
            if(checkFromTo(pistonChunk, ChunkUtil.getChunk(block.getRelative(event.getDirection()).getChunk(), dataService))) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void handlePistonBlockRetract(BlockPistonRetractEvent event) {
        ClaimableChunk pistonChunk = ChunkUtil.getChunk(event.getBlock().getChunk(), dataService);
        event.getBlocks().forEach(block -> {
            if(checkFromTo(pistonChunk, ChunkUtil.getChunk(block.getChunk(), dataService))) {
                event.setCancelled(true);
            }
        });
    }
}
