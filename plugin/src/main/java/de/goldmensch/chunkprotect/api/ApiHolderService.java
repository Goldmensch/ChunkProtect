package de.goldmensch.chunkprotect.api;

import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.HolderService;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ApiHolderService implements HolderService {

  private final ChunkProtectPlugin chunkProtect;
  private final de.goldmensch.chunkprotect.storage.services.HolderService holderService;

  public ApiHolderService(ChunkProtectPlugin chunkProtect) {
    this.chunkProtect = chunkProtect;
    this.holderService = chunkProtect.getDataService();
  }

  @Override
  public CompletableFuture<Boolean> setupHolder(UUID uuid, String name, boolean player) {
    return CompletableFuture.supplyAsync(() -> holderService.setupHolder(uuid, name, player));
  }

  @Override
  public ChunkHolder holderFromUUID(UUID uuid) {
    return holderService.holderFromUUID(uuid);
  }

  @Override
  public CompletableFuture<Void> forceDiscWrite(UUID uuid) {
    return CompletableFuture.runAsync(() -> holderService.saveHolder(uuid),
        chunkProtect.getService());
  }
}
