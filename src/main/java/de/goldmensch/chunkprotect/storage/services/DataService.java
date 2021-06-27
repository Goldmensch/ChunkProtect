package de.goldmensch.chunkprotect.storage.services;

import com.jsoniter.spi.JsoniterSpi;
import de.goldmensch.chunkprotect.core.chunk.*;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import de.goldmensch.chunkprotect.storage.StorageType;
import de.goldmensch.chunkprotect.storage.cache.ChunkCache;
import de.goldmensch.chunkprotect.storage.repositories.chunk.ChunkRepository;
import de.goldmensch.chunkprotect.storage.repositories.chunk.JsonChunkRepository;
import de.goldmensch.chunkprotect.storage.repositories.chunkholder.ChunkHolderRepository;
import de.goldmensch.chunkprotect.storage.repositories.chunkholder.JsonHolderRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public class DataService {

    private final ChunkCache cache;

    private final ChunkRepository chunkRepository;
    private final ChunkHolderRepository chunkHolderRepository;

    private DataService(ChunkRepository chunkRepository, ChunkHolderRepository chunkHolderRepository,
                        ChunkCache cache){
        this.chunkRepository = chunkRepository;
        this.chunkHolderRepository = chunkHolderRepository;
        this.cache = cache;
    }

    public static DataService loadService(Path path, StorageType type) throws IOException {
        ChunkRepository chunkRepository;
        ChunkHolderRepository holderRepository;

        switch (type) {
            case JSON -> {
                JsoniterSpi.registerTypeEncoder(UUID.class, (obj, stream) -> stream.writeVal(obj.toString()));
                JsoniterSpi.registerTypeDecoder(UUID.class, iter -> UUID.fromString(iter.readString()));

                Path chunkDataPath = Files.createDirectories(path.resolve("chunks"));
                chunkRepository= new JsonChunkRepository(chunkDataPath);

                Path holderPath = Files.createDirectories(path.resolve("holders"));
                holderRepository = new JsonHolderRepository(holderPath);
            }
            default -> throw new RuntimeException("failed to create Repositories");
        }

        return new DataService(chunkRepository, holderRepository,
                ChunkCache.initCache(chunkRepository, holderRepository));
    }

    public ChunkCache getCache() {
        return cache;
    }

    public ClaimableChunk getChunkAt(ChunkLocation location) {
        return cache.getChunkAt(location);
    }

    public boolean claimChunk(ChunkLocation location, UUID holderUUID) {
        ChunkHolder holder;
        ClaimedChunk chunk;
        if(getChunkAt(location).isClaimed()) {
            return false;
        }
        holder = cache.getHolder(holderUUID);

        RawClaimedChunk rawClaimedChunk = new RawClaimedChunk(location, holderUUID, new HashSet<>());
        chunk = new ClaimedChunk(rawClaimedChunk, holder);

        chunkRepository.create(rawClaimedChunk);
        cache.getChunkCache().put(location, new ClaimableChunk(chunk));

        if(holder.inNoFallback()) {
            holder.setClaimAmount(holder.getClaimAmount()+1);
            updateHolder(holder);
        }
        return true;
    }

    public boolean unclaimChunk(ChunkLocation location) {
        ClaimableChunk chunk = getChunkAt(location);
        if(chunk.isClaimed()) {
            ChunkHolder holder = chunk.getChunk().getHolder();
            holder.setClaimAmount(holder.getClaimAmount()-1);

            cache.getChunkCache().invalidate(location);
            chunkRepository.delete(location);
            if(holder.inNoFallback()) {
                updateHolder(holder);
            }
        }
        return chunk.isClaimed();
    }

    public void updateChunk(ClaimedChunk chunk) {
        chunkRepository.update(chunk);
        cache.getChunkCache().put(chunk.getLocation(), new ClaimableChunk(chunk));
    }

    // Holder
    public void createOrUpdate(UUID uuid, String name, boolean player) {
        var holderOptional = chunkHolderRepository.read(uuid);
        if(holderOptional.isPresent()) {
            ChunkHolder holder = holderOptional.get();
            if(!holder.getName().equals(name)) {
                holder.setName(name);
                updateHolder(holder);
            }
        }else {
            ChunkHolder holder = chunkHolderRepository.create(new ChunkHolder(name, uuid, 0, player));
            cache.getHolderCache().put(uuid, Optional.of(holder));
        }
    }

    private void updateHolder(ChunkHolder holder) {
        cache.getHolderCache().put(holder.getUuid(), Optional.of(holder));
        chunkHolderRepository.update(holder);
    }

    public ChunkHolder holderFromUUID(UUID uuid) {
        return cache.getHolder(uuid);
    }
}
