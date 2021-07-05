package de.goldmensch.chunkprotect.core.chunk;

public class ClaimableChunk {

    private final ClaimedChunk chunk;

    public ClaimableChunk(ClaimedChunk chunk) {
        this.chunk = chunk;
    }

    public static ClaimableChunk forceClaimed(ChunkLocation location) {
        return new ClaimableChunk(ClaimedChunk.forceClaimed(location));
    }

    public boolean isClaimed() {
        return chunk != null;
    }

    public ClaimedChunk getChunk() {
        return chunk;
    }
}
