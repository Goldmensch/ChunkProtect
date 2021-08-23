package de.goldmensch.chunkprotect;

import java.util.concurrent.CompletableFuture;

public abstract class ChunkProtect {
    private final ChunkService chunkService;
    private final HolderService holderService;
    private final ProtectionBypass protectionBypass;

    public ChunkProtect(ChunkService chunkService, HolderService holderService, ProtectionBypass protectionBypass) {
        this.chunkService = chunkService;
        this.holderService = holderService;
        this.protectionBypass = protectionBypass;
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

    public abstract CompletableFuture<Void> forceSaveAll();
}
