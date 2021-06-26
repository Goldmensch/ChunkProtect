package de.goldmensch.chunkprotect.storage.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.core.chunk.RawClaimedChunk;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.storage.repositories.chunk.ChunkRepository;
import de.goldmensch.chunkprotect.storage.repositories.chunkholder.ChunkHolderRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ChunkCache {

    private final LoadingCache<UUID, Optional<ChunkHolder>> holderCache;
    private final LoadingCache<ChunkLocation, ClaimableChunk> chunkCache;

    private ChunkCache(LoadingCache<UUID, Optional<ChunkHolder>> holderCache, LoadingCache<ChunkLocation, ClaimableChunk> chunkCache) {
        this.holderCache = holderCache;
        this.chunkCache = chunkCache;
    }

    public static ChunkCache initCache(ChunkRepository chunkRepository, ChunkHolderRepository holderRepository) {
        LoadingCache<UUID, Optional<ChunkHolder>> holderCache = CacheBuilder.newBuilder()
                .expireAfterAccess(20, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public Optional<ChunkHolder> load(@NotNull UUID key){
                        return holderRepository.read(key);
                    }
                });

        LoadingCache<ChunkLocation, ClaimableChunk> chunkCache = CacheBuilder.newBuilder()
                .expireAfterAccess(15, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public ClaimableChunk load(@NotNull ChunkLocation key) throws ExecutionException {
                        Optional<RawClaimedChunk> chunkOptional = chunkRepository.read(key);
                        if(chunkOptional.isPresent()) {
                            RawClaimedChunk chunk = chunkOptional.get();
                            ChunkHolder holder;
                            Optional<ChunkHolder> holderOptional = holderCache.get(chunk.getHolderUUID());
                            if(holderOptional.isEmpty()) {
                                holder = ChunkHolder.fallback(chunk.getHolderUUID());
                            }else {
                                holder = holderOptional.get();
                            }

                            return new ClaimableChunk(new ClaimedChunk(chunk, holder));
                        }else {
                            return new ClaimableChunk(null);
                        }
                    }
                });

        return new ChunkCache(holderCache, chunkCache);
    }


    public LoadingCache<ChunkLocation, ClaimableChunk> getChunkCache() {
        return chunkCache;
    }

    public LoadingCache<UUID, Optional<ChunkHolder>> getHolderCache() {
        return holderCache;
    }

    public ChunkHolder getHolder(UUID uuid) {
        try {
            Optional<ChunkHolder> holderOptional = holderCache.get(uuid);
            if(holderOptional.isPresent()) {
                return holderOptional.get();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ChunkHolder.fallback(uuid);
    }

    public ClaimableChunk getChunkAt(ChunkLocation location) {
        try {
            return chunkCache.get(location);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ClaimableChunk.forceClaimed();
    }
}
