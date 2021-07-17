package de.goldmensch.chunkprotect.listener.protect;

import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import de.goldmensch.chunkprotect.configuration.protection.elements.options.FromToChunkOption;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.utils.ChunkUtil;
import de.goldmensch.chunkprotect.storage.services.DataService;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BlockListeners extends ListenerData {
    public BlockListeners(DataService dataService, ChunkProtect chunkProtect) {
        super(dataService, chunkProtect);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBlockPlace(BlockPlaceEvent event) {
        ChunkUtil.getChunk(event.getBlock().getChunk(), dataService).ifClaimed(chunk -> {
            if(forbidden(event.getPlayer(), chunk)) event.setCancelled(true);
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBlockBreak(BlockBreakEvent event) {
        ChunkUtil.getChunk(event.getBlock().getChunk(), dataService).ifClaimed(chunk -> {
            if(forbidden(event.getPlayer(), chunk)) event.setCancelled(true);
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBlockTo(BlockFromToEvent event) {
        FromToChunkOption option = switch (event.getBlock().getType()) {
            case LAVA -> protectionFile.getBlock().getLavaFlow();
            case WATER -> protectionFile.getBlock().getWaterFlow();
            case DRAGON_EGG -> protectionFile.getBlock().getDragonEggTeleport();
            default -> FromToChunkOption.NO_PROTECTION;
        };
        if(fromTo(event.getBlock().getChunk(), event.getToBlock().getChunk(), option)) event.setCancelled(true);
    }

    private boolean fromTo(Chunk fromChunk, Chunk toChunk, FromToChunkOption option) {
        if(fromChunk.equals(toChunk)) return false;
        ClaimableChunk from = ChunkUtil.getChunk(fromChunk, dataService);
        ClaimableChunk to = ChunkUtil.getChunk(toChunk, dataService);
        if(from.isClaimed()) {
            if (option.getFromClaimedInto().isClaimed() && to.isClaimed()) {
                return !ChunkUtil.sameHolder(from.getChunk(), to.getChunk());
            }else return option.getFromClaimedInto().isUnclaimed() && !to.isClaimed();
        }else {
            if (option.getFromUnclaimedInto().isClaimed() && to.isClaimed()) {
                return true;
            }else return option.getFromUnclaimedInto().isUnclaimed() && !to.isClaimed();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handlePistonBlockExtend(BlockPistonExtendEvent event) {
        event.getBlocks().forEach(block -> {
            if(fromTo(block.getChunk(), block.getRelative(event.getDirection()).getChunk(), protectionFile.getBlock().getPistonExtend())) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handlePistonBlockRetract(BlockPistonRetractEvent event) {
        event.getBlocks().forEach(block -> {
            if(fromTo(block.getChunk(), block.getChunk(), protectionFile.getBlock().getPistonRetract())) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBlockExplode(BlockExplodeEvent event) {
        onExplodeEvent(event.blockList());
    }

    protected void onExplodeEvent(List<Block> blockList) {
        Set<Block> protectedBlocks = new HashSet<>();
        for(Block block : blockList) {
            ChunkUtil.getChunk(block.getChunk(), dataService).ifClaimedOr(chunk -> {
                if(protectionFile.getBlock().getBlockBreakByExplosion().isClaimed()) {
                    protectedBlocks.add(block);
                }
            }, () -> {
                if(protectionFile.getBlock().getBlockBreakByExplosion().isUnclaimed()) {
                    protectedBlocks.add(block);
                }
            });
        }
        blockList.removeAll(protectedBlocks);
    }

    @EventHandler(priority = EventPriority.HIGH)
    protected void handleTntPrime(TNTPrimeEvent event) {
        unwrapPlayer(event.getPrimerEntity()).ifPresent(player ->
                ChunkUtil.getChunk(event.getBlock().getChunk(), dataService).ifClaimedOr(chunk -> {
            if(protectionFile.getBlock().getTntPrime().isClaimed() && forbidden(player, chunk)) {
                event.setCancelled(true);
            }
        }, () -> {
            if(protectionFile.getBlock().getTntPrime().isUnclaimed()) {
                sendYouCantDoThat(player);
            }
        }));
    }
}
