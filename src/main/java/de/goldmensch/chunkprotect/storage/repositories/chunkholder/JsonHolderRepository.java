package de.goldmensch.chunkprotect.storage.repositories.chunkholder;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public class JsonHolderRepository implements ChunkHolderRepository{

    private final Path path;

    public JsonHolderRepository(Path path) {
        this.path = path;
    }

    @Override
    public ChunkHolder create(ChunkHolder holder) {
        Path local = buildPath(holder.getUuid());

        try {
            Files.createFile(local);
            Files.writeString(local, JsonStream.serialize(holder));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return holder;
    }

    @Override
    public void update(ChunkHolder holder) {
        try {
            Files.writeString(buildPath(holder.getUuid()), JsonStream.serialize(holder));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID uuid) {
        try {
            Files.delete(buildPath(uuid));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<ChunkHolder> read(UUID uuid) {
        Path local = buildPath(uuid);

        if(Files.exists(local)) {
            try {
                return Optional.of(JsonIterator.deserialize(Files.readAllBytes(local), ChunkHolder.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    private Path buildPath(UUID uuid) {
        return path.resolve("holder-UUID_" + uuid + ".json");
    }
}
