package de.goldmensch.chunkprotect.storage.services;

import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.core.chunk.RawClaimedChunk;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import de.goldmensch.chunkprotect.storage.cache.Cache;
import de.goldmensch.chunkprotect.storage.repositories.chunk.ChunkDao;
import de.goldmensch.chunkprotect.storage.repositories.holder.HolderDao;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public class ChunkService extends HolderService{

    final ChunkDao chunkDao;

    public ChunkService(Cache cache, HolderDao holderDao, ChunkDao chunkDao) {
        super(cache, holderDao);
        this.chunkDao = chunkDao;
    }

    public ClaimableChunk getChunkAt(ChunkLocation location) {
        return cache.get(location).orElse(ClaimableChunk.forceClaimed(location));
    }

    public boolean claimChunk(ChunkLocation location, UUID holderUUID) {
        ChunkHolder holder;
        ClaimedChunk chunk;
        if(getChunkAt(location).isClaimed()) {
            return false;
        }
        holder = holderFromUUID(holderUUID);
        chunk = new ClaimedChunk(new RawClaimedChunk(location, holderUUID, new HashSet<>()), holder);
        cache.set(location, new ClaimableChunk(chunk));
        holder.getClaimedChunks().add(chunk.getLocation());
        updateHolder(holder);
        return true;
    }

    public boolean unclaimChunk(ChunkLocation location) {
        ClaimableChunk chunk = getChunkAt(location);
        if(chunk.isClaimed()) {
            cache.set(location, new ClaimableChunk(null));
            ChunkHolder holder = chunk.getChunk().getHolder();
            holder.getClaimedChunks().remove(location);
            updateHolder(holder);
        }
        return chunk.isClaimed();
    }

    public void loadChunkIfUnloaded(ChunkLocation location) {
        if(cache.isCached(location)) return;
        ClaimedChunk claimedChunk = null;
        Optional<RawClaimedChunk> rawClaimedChunkOptional = chunkDao.read(location);
        if(rawClaimedChunkOptional.isPresent()) {
            RawClaimedChunk rawClaimedChunk = rawClaimedChunkOptional.get();
            ChunkHolder chunkHolder = holderFromUUID(rawClaimedChunk.getHolderUUID());
            claimedChunk = new ClaimedChunk(rawClaimedChunk, chunkHolder);
        }
        ClaimableChunk claimableChunk = new ClaimableChunk(claimedChunk);
        cache.set(location, claimableChunk);
    }

    public void updateChunk(ClaimedChunk chunk) {
        cache.set(chunk.getLocation(), new ClaimableChunk(chunk));
    }

    public void writeAndInvalidate(ChunkLocation location) {
        ClaimableChunk chunk = getChunkAt(location);
        write(chunk, location);
        cache.invalidate(location);
    }

    public void write(ClaimableChunk claimableChunk, ChunkLocation location) {
        if(claimableChunk.isClaimed()) {
            ClaimedChunk chunk = claimableChunk.getChunk();
            if(cache.isCached(location) && chunk.notForceClaimed()) {
                chunkDao.write(chunk);
            }
        }else {
            chunkDao.delete(location);
        }
    }
}
