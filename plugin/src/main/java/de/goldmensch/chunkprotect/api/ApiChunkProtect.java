package de.goldmensch.chunkprotect.api;

import de.goldmensch.chunkprotect.ChunkProtect;
import de.goldmensch.chunkprotect.ChunkProtectPlugin;

import java.util.concurrent.CompletableFuture;

public class ApiChunkProtect extends ChunkProtect {

    private final ChunkProtectPlugin chunkProtect;

    public ApiChunkProtect(ChunkProtectPlugin chunkProtect) {
        super(new ApiChunkService(chunkProtect), new ApiHolderService(chunkProtect), chunkProtect.getProtectionBypass());
        this.chunkProtect = chunkProtect;
    }

    @Override
    public CompletableFuture<Void> forceSaveAll() {
        return CompletableFuture.runAsync(() -> chunkProtect.getDataService().saveAll(false), chunkProtect.getService());
    }
}
