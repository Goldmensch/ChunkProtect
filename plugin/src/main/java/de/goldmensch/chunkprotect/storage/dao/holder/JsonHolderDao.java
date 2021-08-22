package de.goldmensch.chunkprotect.storage.dao.holder;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public class JsonHolderDao implements HolderDao {

    private final Path path;

    public JsonHolderDao(Path path) {
        this.path = path;
    }

    @Override
    public void write(ChunkHolder holder) {
        Path local = buildPath(holder.getUuid());
        try {
            if (Files.notExists(local)) {
                Files.createFile(local);
            }
            if (Files.isWritable(local)) {
                Files.writeString(local, JsonStream.serialize(holder));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID uuid) {
        Path local = buildPath(uuid);
        try {
            if (Files.isWritable(local)) {
                Files.delete(local);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<ChunkHolder> read(UUID uuid) {
        Path local = buildPath(uuid);

        if (Files.exists(local)) {
            try {
                return Optional.of(JsonIterator.deserialize(Files.readAllBytes(local), ChunkHolder.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    private Path buildPath(UUID uuid) {
        return path.resolve(uuid + ".json");
    }
}
