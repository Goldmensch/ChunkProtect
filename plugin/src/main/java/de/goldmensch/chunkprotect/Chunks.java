package de.goldmensch.chunkprotect;

import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.message.Messenger;
import de.goldmensch.chunkprotect.storage.services.DataService;
import de.goldmensch.smartutils.localizer.Replacement;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class Chunks {

    private Chunks() {
    }

    public static boolean isHolder(ClaimedChunk chunk, UUID holder) {
        return chunk.getHolderUUID().equals(holder);
    }

    public static boolean hasAccess(ClaimedChunk chunk, UUID holder) {
        return chunk.getTrustedPlayer().contains(holder)
                || chunk.getHolderUUID().equals(holder) || chunk.getHolder().getTrustedAllChunks().contains(holder);
    }

    public static ClaimableChunk chunkFromSenderUnsafe(CommandSender sender, DataService service) {
        return service.getChunkAt(ChunkLocation.fromChunk(((Player) sender).getChunk()));
    }

    public static ClaimableChunk chunkFromPlayer(Player player, DataService service) {
        return chunkFromSenderUnsafe(player, service);
    }

    public static ClaimableChunk getChunk(Chunk chunk, DataService dataService) {
        return dataService.getChunkAt(ChunkLocation.fromChunk(chunk));
    }

    public static boolean sameHolder(ClaimedChunk first, ClaimedChunk second) {
        return first.getHolderUUID().equals(second.getHolderUUID());
    }

    public static boolean isClaimedAndHolder(ClaimableChunk chunk, Player sender, ChunkProtectPlugin protect) {
        Messenger messenger = protect.getMessenger();
        if (!chunk.isClaimed()) {
            messenger.send(sender, "chunk-not-claimed");
            return true;
        }
        ClaimedChunk claimedChunk = chunk.getChunk();
        if (!Chunks.isHolder(claimedChunk, sender.getUniqueId())) {
            messenger.send(sender, "not-owner-from-chunk", Replacement.create("holder",
                    claimedChunk.getHolder().getName()));
            return true;
        }
        return false;
    }
}
