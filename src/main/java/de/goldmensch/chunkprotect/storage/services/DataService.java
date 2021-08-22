package de.goldmensch.chunkprotect.storage.services;

import com.jsoniter.spi.JsoniterSpi;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.storage.cache.Cache;
import de.goldmensch.chunkprotect.storage.dao.chunk.ChunkDao;
import de.goldmensch.chunkprotect.storage.dao.chunk.JsonChunkDao;
import de.goldmensch.chunkprotect.storage.dao.holder.HolderDao;
import de.goldmensch.chunkprotect.storage.dao.holder.JsonHolderDao;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class DataService extends ChunkService {

    public DataService(Cache cache, HolderDao holderDao, ChunkDao chunkDao) {
        super(cache, holderDao, chunkDao);
    }

    public static DataService loadService(ChunkProtect protect) throws IOException {
        Path path = protect.getDataFolder().toPath().resolve("data");
        JsoniterSpi.registerTypeEncoder(UUID.class, (obj, stream) -> stream.writeVal(obj.toString()));
        JsoniterSpi.registerTypeDecoder(UUID.class, iter -> UUID.fromString(iter.readString()));

        HolderDao holderDao = new JsonHolderDao(Files.createDirectories(path.resolve("holders")));
        DataService dataService = new DataService(Cache.init(holderDao), holderDao,
                new JsonChunkDao(Files.createDirectories(path.resolve("chunks"))));
        dataService.initWriteScheduler(protect, protect.getConfigFile().getStorage().getSaveInterval());
        return dataService;
    }

    private void initWriteScheduler(Plugin plugin, int interval) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> saveAll(false), 0, interval * 60 * 20L);
    }

    public void saveAll(boolean invalidate) {
        cache.getAllChunks().forEach(entry -> {
            if (invalidate) {
                writeAndInvalidate(entry.getKey());
            } else {
                write(entry.getValue(), entry.getKey());
            }
        });

        cache.getAllHolder().forEach(entry -> {
            write(entry.getValue());
            if (invalidate && entry.getValue().getClaimedChunks()
                    .stream()
                    .noneMatch(cache::isCached)) {
                cache.invalidate(entry.getKey());
            }
        });
    }
}
