package de.goldmensch.chunkprotect.storage.repositories.holder;


import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import de.goldmensch.chunkprotect.storage.repositories.Dao;

import java.util.UUID;

public interface HolderDao extends Dao<UUID, ChunkHolder> {
}
