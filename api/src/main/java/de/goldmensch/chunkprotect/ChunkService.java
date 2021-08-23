package de.goldmensch.chunkprotect;

import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ChunkService {
    ClaimableChunk getChunkAt(ChunkLocation location);
    boolean isCached(ChunkLocation location);
    boolean claimChunk(ChunkLocation location, UUID holderUUID);
    boolean unclaimChunk(ChunkLocation location);
    CompletableFuture<Boolean> loadChunkIfUnloaded(ChunkLocation location);
    CompletableFuture<Void> forceDiscWrite(ClaimableChunk chunk);
}
