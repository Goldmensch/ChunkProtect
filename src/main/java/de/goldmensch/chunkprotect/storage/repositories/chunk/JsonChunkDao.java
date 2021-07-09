package de.goldmensch.chunkprotect.storage.repositories.chunk;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.core.chunk.RawClaimedChunk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class JsonChunkDao implements ChunkDao {

    private final Path path;

    public JsonChunkDao(Path path) {
        this.path = path;
    }

    @Override
    public void write(RawClaimedChunk chunk) {
        Path local = buildPath(chunk.getLocation());
        try {
            if(Files.notExists(local)) {
                Files.createFile(local);
            }
            if(Files.isWritable(local)) {
                Files.writeString(local, JsonStream.serialize(chunk));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ChunkLocation location) {
        Path local = buildPath(location);
        try {
            if(Files.isWritable(local)) {
                Files.delete(local);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<RawClaimedChunk> read(ChunkLocation location) {
        Path local = buildPath(location);

        if(Files.exists(local)) {
            try {
                return Optional.of(JsonIterator.deserialize(Files.readAllBytes(local), RawClaimedChunk.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    private Path buildPath(ChunkLocation location) {
        return path.resolve(
                        "X-" + location.getX() +
                        ".Z-" + location.getZ() +
                        ".WORLD-" + location.getWorld() + ".json");
    }
}