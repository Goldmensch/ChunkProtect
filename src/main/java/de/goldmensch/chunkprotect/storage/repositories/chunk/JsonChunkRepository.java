package de.goldmensch.chunkprotect.storage.repositories.chunk;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.core.chunk.RawClaimedChunk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class JsonChunkRepository implements ChunkRepository {

    private final Path path;

    public JsonChunkRepository(Path path) {
        this.path = path;
    }

    @Override
    public RawClaimedChunk create(RawClaimedChunk chunk) {
        Path local = buildPath(chunk.getLocation());

        try {
            Files.createFile(local);
            Files.writeString(local, JsonStream.serialize(chunk));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chunk;
    }

    @Override
    public void update(RawClaimedChunk chunk) {
        try {
            Files.writeString(buildPath(chunk.getLocation()), JsonStream.serialize(chunk));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ChunkLocation location) {
        try {
            Files.delete(buildPath(location));
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
        return path.resolve("chunk-X" +
                location.getX() +
                "Z" +
                location.getZ() + ".json");
    }
}