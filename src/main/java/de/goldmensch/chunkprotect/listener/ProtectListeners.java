package de.goldmensch.chunkprotect.listener;

import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.core.chunk.util.ChunkUtil;
import de.goldmensch.chunkprotect.storage.services.DataService;
import de.goldmensch.smartutils.localizer.Replacement;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class ProtectListeners implements Listener {

    private final DataService dataService;
    private final ChunkProtect chunkProtect;

    public ProtectListeners(ChunkProtect chunkProtect) {
        this.dataService = chunkProtect.getDataService();
        this.chunkProtect = chunkProtect;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBlockPlace(BlockPlaceEvent event) {
        if(access(event.getPlayer(), event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBlockBreak(BlockBreakEvent event) {
        if(access(event.getPlayer(), event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
    }

    private boolean access(Player player, Chunk chunk) {
        ClaimableChunk claimableChunk = dataService.getChunkAt(ChunkLocation.fromChunk(chunk));
        if(claimableChunk.isClaimed()) {
            ClaimedChunk claimedChunk = claimableChunk.getChunk();
            if(!ChunkUtil.hasAccess(claimedChunk, player.getUniqueId())) {
                sendNoAccess(player, claimedChunk);
                return true;
            }
        }
        return false;
    }

    private void sendNoAccess(Player player, ClaimedChunk chunk) {
        chunkProtect.getMessenger().send(player, "chunk-protected",
                Replacement.create("holder", chunk.getHolder().getName()));
    }



}
