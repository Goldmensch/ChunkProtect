package de.goldmensch.chunkprotect.storage.repositories.chunk;

import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.core.chunk.RawClaimedChunk;
import de.goldmensch.chunkprotect.storage.repositories.Dao;

public interface ChunkDao extends Dao<ChunkLocation, RawClaimedChunk> {

}
