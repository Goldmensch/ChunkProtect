package de.goldmensch.chunkprotect.storage.dao.chunk;

import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.core.chunk.RawClaimedChunk;
import de.goldmensch.chunkprotect.storage.dao.Dao;

public interface ChunkDao extends Dao<ChunkLocation, RawClaimedChunk> {

}
