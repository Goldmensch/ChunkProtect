package de.goldmensch.chunkprotect.core.chunk;

import de.goldmensch.chunkprotect.core.holder.ChunkHolder;

public class ClaimedChunk extends RawClaimedChunk {

    private final ChunkHolder holder;

    public ClaimedChunk(RawClaimedChunk chunk, ChunkHolder holder) {
        super(chunk.getLocation(), chunk.getHolderUUID(), chunk.getTrustedPlayer());
        this.holder = holder;
    }

    public ChunkHolder getHolder() {
        return holder;
    }
}
