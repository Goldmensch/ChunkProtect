package de.goldmensch.chunkprotect.storage.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.storage.repositories.holder.HolderDao;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Cache {

    private final LoadingCache<UUID, ChunkHolder> holderCache;
    private final Map<ChunkLocation, ClaimableChunk> chunkCache = new ConcurrentHashMap<>();

    private Cache(LoadingCache<UUID, ChunkHolder> holderCache) {
        this.holderCache = holderCache;
    }

    public static Cache init(HolderDao repository) {
        return new Cache(CacheBuilder.newBuilder()
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public ChunkHolder load(@NotNull UUID key){
                        return repository.read(key).orElse(ChunkHolder.fallback(key));
                    }
                })
        );
    }

    public boolean isCached(ChunkLocation location) {
        return chunkCache.containsKey(location);
    }

    public boolean isCached(UUID uuid) {
        return holderCache.asMap().containsKey(uuid);
    }

    public ChunkHolder getSave(UUID uuid) {
        try {
            return holderCache.get(uuid);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return ChunkHolder.fallback(uuid);
        }
    }

    public Optional<ClaimableChunk> get(ChunkLocation location) {
        return Optional.ofNullable(chunkCache.get(location));
    }

    public void set(ChunkHolder holder) {
        holderCache.put(holder.getUuid(), holder);
    }

    public void set(ChunkLocation location, ClaimableChunk chunk) {
        chunkCache.put(location, chunk);
    }

    public void invalidate(ChunkLocation location) {
        chunkCache.remove(location);
    }

    public Set<Map.Entry<UUID, ChunkHolder>> getAllHolder() {
        return holderCache.asMap().entrySet();
    }

    public Set<Map.Entry<ChunkLocation, ClaimableChunk>> getAllChunks() {
        return chunkCache.entrySet();
    }
}
