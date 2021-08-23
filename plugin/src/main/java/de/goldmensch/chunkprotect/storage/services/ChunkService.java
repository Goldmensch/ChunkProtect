package de.goldmensch.chunkprotect.storage.services;

import de.goldmensch.chunkprotect.ChunkLocation;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.core.chunk.RawClaimedChunk;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import de.goldmensch.chunkprotect.storage.cache.Cache;
import de.goldmensch.chunkprotect.storage.dao.chunk.ChunkDao;
import de.goldmensch.chunkprotect.storage.dao.holder.HolderDao;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public class ChunkService extends HolderService {

    final ChunkDao chunkDao;

    public ChunkService(Cache cache, HolderDao holderDao, ChunkDao chunkDao) {
        super(cache, holderDao);
        this.chunkDao = chunkDao;
    }

    public ClaimableChunk getChunkAt(ChunkLocation location) {
        return cache.get(location).orElse(ClaimableChunk.forceClaimed(location));
    }

    public boolean claimChunk(ChunkLocation location, UUID holderUUID) {
        if (getChunkAt(location).isClaimed()) {
            return false;
        }
        ChunkHolder holder = holderFromUUID(holderUUID);
        ClaimedChunk chunk = new ClaimedChunk(new RawClaimedChunk(location, holderUUID, new HashSet<>()), holder);
        cache.set(location, new ClaimableChunk(chunk, location));
        holder.getClaimedChunks().add(chunk.getLocation());
        return true;
    }


    public boolean unclaimChunk(ChunkLocation location) {
        ClaimableChunk chunk = getChunkAt(location);
        if (chunk.isClaimed()) {
            cache.set(location, new ClaimableChunk(null, location));
            ChunkHolder holder = chunk.getChunk().getHolder();
            holder.getClaimedChunks().remove(location);
        }
        return chunk.isClaimed();
    }

    public boolean loadChunkIfUnloaded(ChunkLocation location) {
        if (cache.isCached(location)) return false;
        ClaimedChunk claimedChunk = null;
        Optional<RawClaimedChunk> rawClaimedChunkOptional = chunkDao.read(location);
        if (rawClaimedChunkOptional.isPresent()) {
            RawClaimedChunk rawClaimedChunk = rawClaimedChunkOptional.get();
            ChunkHolder chunkHolder = holderFromUUID(rawClaimedChunk.getHolderUUID());
            claimedChunk = new ClaimedChunk(rawClaimedChunk, chunkHolder);
        }
        ClaimableChunk claimableChunk = new ClaimableChunk(claimedChunk, location);
        cache.set(location, claimableChunk);
        return true;
    }

    public void writeAndInvalidate(ChunkLocation location) {
        write(getChunkAt(location));
        cache.invalidate(location);
    }

    public void write(ClaimableChunk claimableChunk) {
        ChunkLocation location = claimableChunk.getLocation();
        if (claimableChunk.isClaimed()) {
            ClaimedChunk chunk = claimableChunk.getChunk();
            if (cache.isCached(location) && chunk.notForceClaimed()) {
                chunkDao.write(chunk);
            }
        } else {
            chunkDao.delete(location);
        }
    }
}
