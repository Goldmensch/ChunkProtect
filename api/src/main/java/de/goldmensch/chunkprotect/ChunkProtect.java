package de.goldmensch.chunkprotect;

import de.goldmensch.chunkprotect.message.IMessenger;
import java.util.concurrent.CompletableFuture;

public abstract class ChunkProtect {

  private final ChunkService chunkService;
  private final HolderService holderService;
  private final ProtectionBypass protectionBypass;
  private final IMessenger messenger;

  public ChunkProtect(ChunkService chunkService, HolderService holderService,
                      ProtectionBypass protectionBypass, IMessenger messenger) {
    this.chunkService = chunkService;
    this.holderService = holderService;
    this.protectionBypass = protectionBypass;
    this.messenger = messenger;
  }

  public ChunkService getChunkService() {
    return chunkService;
  }

  public HolderService getHolderService() {
    return holderService;
  }

  public ProtectionBypass getProtectionBypass() {
    return protectionBypass;
  }

  public IMessenger getMessenger() {
    return messenger;
  }

  public abstract CompletableFuture<Void> forceSaveAll();
}
