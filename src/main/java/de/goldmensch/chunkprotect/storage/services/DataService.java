package de.goldmensch.chunkprotect.storage.services;

import com.jsoniter.spi.JsoniterSpi;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.storage.cache.Cache;
import de.goldmensch.chunkprotect.storage.repositories.chunk.ChunkDao;
import de.goldmensch.chunkprotect.storage.repositories.chunk.JsonChunkDao;
import de.goldmensch.chunkprotect.storage.repositories.holder.HolderDao;
import de.goldmensch.chunkprotect.storage.repositories.holder.JsonHolderDao;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DataService extends ChunkService{

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
        dataService.initWriteScheduler(protect);
        return dataService;
    }

    private void initWriteScheduler(Plugin plugin) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::safeAll, 0, 6000);
    }

    public void safeAll() {
        cache.getAllChunks().forEach(entry -> write(entry.getValue(), entry.getKey()));
        cache.getAllHolder().forEach(entry -> write(entry.getValue()));
    }
}
