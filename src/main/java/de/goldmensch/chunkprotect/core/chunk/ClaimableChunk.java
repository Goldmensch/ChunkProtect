package de.goldmensch.chunkprotect.core.chunk;

public class ClaimableChunk {

    private final ClaimedChunk chunk;
    private boolean claimed;

    public ClaimableChunk(ClaimedChunk chunk) {
        this.chunk = chunk;
        if(chunk != null) {
            claimed = true;
        }
    }

    private ClaimableChunk(ClaimedChunk chunk, boolean claimed) {
        this.chunk = chunk;
    }

    public static ClaimableChunk forceClaimed() {
        return new ClaimableChunk(null, true);
    }

    public boolean isClaimed() {
        return claimed;
    }

    public ClaimedChunk getChunk() {
        return chunk;
    }
}
