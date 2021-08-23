package de.goldmensch.chunkprotect;

import de.goldmensch.chunkprotect.core.holder.ChunkHolder;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface HolderService {
    CompletableFuture<Boolean> setupHolder(UUID uuid, String name, boolean player);
    ChunkHolder holderFromUUID(UUID uuid);
    CompletableFuture<Void> forceDiscWrite(UUID uuid);
}
