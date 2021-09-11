package de.goldmensch.chunkprotect.storage.services;

import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import de.goldmensch.chunkprotect.storage.cache.Cache;
import de.goldmensch.chunkprotect.storage.dao.holder.HolderDao;
import java.util.UUID;

public class HolderService {

  final Cache cache;
  final HolderDao holderDao;

  public HolderService(Cache cache, HolderDao holderDao) {
    this.cache = cache;
    this.holderDao = holderDao;
  }

  public boolean setupHolder(UUID uuid, String name, boolean player) {
    ChunkHolder holder;
    var holderOptional = holderDao.read(uuid);

    if (holderOptional.isPresent()) {
      holder = holderOptional.get();
      if (name != null) {
        holder.updateName(name);
      }
    } else {
      holder = new ChunkHolder(name, uuid, player);
    }
    cache.set(holder);
    return holderOptional.isEmpty();
  }

  public void saveHolder(UUID uuid) {
    var holder = holderFromUUID(uuid);
    if (holder.noFallback()) {
      holderDao.write(holder);
    }
  }

  public ChunkHolder holderFromUUID(UUID uuid) {
    return cache.getSave(uuid);
  }

  public void write(ChunkHolder holder) {
    if (cache.isCached(holder.getUuid()) && holder.noFallback()) {
      holderDao.write(holder);
    }
  }
}
