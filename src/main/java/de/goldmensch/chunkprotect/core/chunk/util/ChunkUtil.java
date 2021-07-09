package de.goldmensch.chunkprotect.core.chunk.util;

import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.storage.services.DataService;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChunkUtil {

    public static boolean isHolder(ClaimedChunk chunk, UUID holder) {
        return chunk.getHolderUUID().equals(holder);
    }

    public static boolean hasAccess(ClaimedChunk chunk, UUID holder) {
        return chunk.getTrustedPlayer().contains(holder)
                || chunk.getHolderUUID().equals(holder);
    }

    public static ClaimableChunk chunkFromSenderUnsafe(CommandSender sender, DataService service) {
        return service.getChunkAt(ChunkLocation.fromChunk(((Player)sender).getChunk()));
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
}
