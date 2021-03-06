package de.goldmensch.chunkprotect.storage.services;

import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import de.goldmensch.chunkprotect.storage.cache.Cache;
import de.goldmensch.chunkprotect.storage.repositories.holder.HolderDao;

import java.util.UUID;

public class HolderService {

    final Cache cache;
    final HolderDao holderDao;

    public HolderService(Cache cache, HolderDao holderDao) {
        this.cache = cache;
        this.holderDao = holderDao;
    }

    public void setupHolder(UUID uuid, String name, boolean player) {
        ChunkHolder holder;

        var holderOptional = holderDao.read(uuid);
        if(holderOptional.isPresent()) {
            holder = holderOptional.get();
            holder.setName(name);
        }else {
            holder = new ChunkHolder(name, uuid, 0, player);
        }
        updateHolder(holder);
    }

    public void saveHolder(UUID uuid) {
        var holder = holderFromUUID(uuid);
        if(holder.isNoFallback()) {
            holderDao.write(holder);
        }
    }

    public void updateHolder(ChunkHolder holder) {
        cache.set(holder);
    }

    public ChunkHolder holderFromUUID(UUID uuid) {
        return cache.getSave(uuid);
    }

    public void write(ChunkHolder holder) {
        if(cache.isCached(holder.getUuid()) && holder.isNoFallback()) {
            holderDao.write(holder);
        }
    }
}
