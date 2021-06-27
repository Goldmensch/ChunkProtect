package de.goldmensch.chunkprotect.commands.util;

import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.core.chunk.util.ChunkUtil;
import de.goldmensch.chunkprotect.message.Messenger;
import de.goldmensch.smartutils.localizer.Replacement;
import org.bukkit.entity.Player;

public class CmdUtil {

    public static boolean isClaimedAndHolder(ClaimableChunk chunk, Player sender, ChunkProtect protect) {
        Messenger messenger = protect.getMessenger();
        if(!chunk.isClaimed()) {
            messenger.send(sender, "chunk-not-claimed");
            return true;
        }
        ClaimedChunk claimedChunk = chunk.getChunk();
        if(!ChunkUtil.isHolder(claimedChunk, sender.getUniqueId())) {
            messenger.send(sender, "not-owner-from-chunk", Replacement.create("holder",
                    claimedChunk.getHolder().getName()));
            return true;
        }
        return false;
    }
}
