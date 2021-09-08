package de.goldmensch.chunkprotect.api;

import de.goldmensch.chunkprotect.ChunkLocation;
import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.ChunkService;
import de.goldmensch.chunkprotect.Status;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ApiChunkService implements ChunkService {

  private final ChunkProtectPlugin chunkProtect;
  private final de.goldmensch.chunkprotect.storage.services.ChunkService chunkService;

  public ApiChunkService(ChunkProtectPlugin chunkProtect) {
    this.chunkProtect = chunkProtect;
    chunkService = chunkProtect.getDataService();
  }

  @Override
  public ClaimableChunk getChunkAt(ChunkLocation location) {
    return chunkService.getChunkAt(location);
  }

  @Override
  public boolean isCached(ChunkLocation location) {
    return chunkProtect.getDataService().getCache().isCached(location);
  }

  @Override
  public Status claimChunk(ChunkLocation location, UUID holderUUID) {
    return chunkService.claimChunk(location, holderUUID);
  }

  @Override
  public Status unclaimChunk(ChunkLocation location) {
    return chunkService.unclaimChunk(location);
  }

  @Override
  public CompletableFuture<Boolean> loadChunkIfUnloaded(ChunkLocation location) {
    return CompletableFuture.supplyAsync(() -> chunkService.loadChunkIfUnloaded(location),
        chunkProtect.getService());
  }

  @Override
  public CompletableFuture<Void> forceDiscWrite(ClaimableChunk chunk) {
    return CompletableFuture.runAsync(() -> chunkService.write(chunk), chunkProtect.getService());
  }
}
