package de.goldmensch.chunkprotect.core.chunk.util;

import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;

import java.util.UUID;

public class ChunkUtil {

    public static boolean isHolder(ClaimedChunk chunk, UUID holder) {
        return chunk.getHolderUUID().equals(holder);
    }

    public static boolean hasAccess(ClaimedChunk chunk, UUID holder) {
        return chunk.getTrustedPlayer().contains(holder)
                || chunk.getHolderUUID().equals(holder);
    }
}
