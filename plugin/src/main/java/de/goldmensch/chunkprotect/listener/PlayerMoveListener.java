package de.goldmensch.chunkprotect.listener;

import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.Chunks;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.smartutils.localizer.Replacement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    private final ChunkProtectPlugin chunkProtect;

    public PlayerMoveListener(ChunkProtectPlugin chunkProtectPlugin) {
        this.chunkProtect = chunkProtectPlugin;
    }

    @EventHandler
    public void handlePlayerMove(PlayerMoveEvent event) {
        if(event.getFrom().getChunk() != event.getTo().getChunk()
                && chunkProtect.getConfigFile().getNotification().isOnEnterClaim()) {

            Player player = event.getPlayer();
            ClaimableChunk from = Chunks.getChunk(event.getFrom().getChunk(), chunkProtect.getDataService());
            ClaimableChunk to = Chunks.getChunk(event.getTo().getChunk(), chunkProtect.getDataService());

            if(to.isClaimed()) {
                ClaimedChunk toChunk = to.getChunk();
                if(from.isClaimed() && Chunks.sameHolder(toChunk, from.getChunk())) return;
                sendEnterMessage(player, toChunk.getHolder().getName());
             }
        }
    }

    private void sendEnterMessage(Player player, String holder) {
        player.sendActionBar(chunkProtect.getMessenger().prepare("player.enterChunk", Replacement.create("player", holder)));
    }
}
