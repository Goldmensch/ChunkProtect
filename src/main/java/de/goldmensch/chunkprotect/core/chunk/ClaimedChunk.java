package de.goldmensch.chunkprotect.core.chunk;

import com.jsoniter.annotation.JsonIgnore;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;

public class ClaimedChunk extends RawClaimedChunk {

    @JsonIgnore
    private final ChunkHolder holder;

    public ClaimedChunk(RawClaimedChunk chunk, ChunkHolder holder) {
        super(chunk.getLocation(), chunk.getHolderUUID(), chunk.getTrustedPlayer());
        this.holder = holder;
    }

    @JsonIgnore
    public ChunkHolder getHolder() {
        return holder;
    }
}
