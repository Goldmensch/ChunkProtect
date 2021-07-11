package de.goldmensch.chunkprotect.core.chunk;

import com.jsoniter.annotation.JsonIgnore;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;

import java.util.HashSet;
import java.util.UUID;

public class ClaimedChunk extends RawClaimedChunk {

    private final static ChunkHolder FORCE_HOLDER = new ChunkHolder("chunk_forceClaimed",
            UUID.fromString("8534a7ba-9aa6-4bbe-a93a-3632a9781f53"),
            false,
            true);

    @JsonIgnore
    private final ChunkHolder holder;
    @JsonIgnore
    private boolean forceClaimed = false;

    public ClaimedChunk(RawClaimedChunk chunk, ChunkHolder holder) {
        super(chunk.getLocation(), chunk.getHolderUUID(), chunk.getTrustedPlayer());
        this.holder = holder;
    }

    public static ClaimedChunk forceClaimed(ChunkLocation location) {
        return new ClaimedChunk(
                new RawClaimedChunk(location, FORCE_HOLDER.getUuid(), new HashSet<>()),
                FORCE_HOLDER
        ).forceClaimed();
    }

    @JsonIgnore
    public ChunkHolder getHolder() {
        return holder;
    }

    @JsonIgnore
    public boolean notForceClaimed() {
        return !forceClaimed;
    }

    public ClaimedChunk forceClaimed() {
        forceClaimed = true;
        return this;
    }
}
