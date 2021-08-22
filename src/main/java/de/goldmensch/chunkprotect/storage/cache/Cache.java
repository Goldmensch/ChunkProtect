package de.goldmensch.chunkprotect.storage.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import de.goldmensch.chunkprotect.storage.dao.holder.HolderDao;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public final class Cache {

    private final LoadingCache<UUID, ChunkHolder> holderCache;
    private final Map<ChunkLocation, ClaimableChunk> chunkCache = new ConcurrentHashMap<>();

    private Cache(LoadingCache<UUID, ChunkHolder> holderCache) {
        this.holderCache = holderCache;
    }

    public static Cache init(HolderDao repository) {
        return new Cache(CacheBuilder.newBuilder()
                .build(new UUIDChunkHolderCacheLoader(repository))
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

    public void invalidate(UUID uuid) {
        holderCache.invalidate(uuid);
    }

    public Set<Map.Entry<UUID, ChunkHolder>> getAllHolder() {
        return holderCache.asMap().entrySet();
    }

    public Set<Map.Entry<ChunkLocation, ClaimableChunk>> getAllChunks() {
        return chunkCache.entrySet();
    }

    private static final class UUIDChunkHolderCacheLoader extends CacheLoader<UUID, ChunkHolder> {
        private final HolderDao repository;

        private UUIDChunkHolderCacheLoader(HolderDao repository) {
            this.repository = repository;
        }

        @Override
        public ChunkHolder load(@NotNull UUID key) {
            return repository.read(key).orElse(ChunkHolder.fallback(key));
        }
    }
}
