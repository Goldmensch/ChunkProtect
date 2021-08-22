package de.goldmensch.chunkprotect.core.chunk;

import de.goldmensch.chunkprotect.ChunkLocation;

import java.util.function.Consumer;

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

    public void ifClaimed(Consumer<ClaimedChunk> function) {
        if (isClaimed()) {
            function.accept(chunk);
        }
    }

    public void ifClaimedOr(Consumer<ClaimedChunk> function, Runnable or) {
        if (isClaimed()) {
            function.accept(chunk);
        } else {
            or.run();
        }
    }

    public ClaimedChunk getChunk() {
        return chunk;
    }
}
