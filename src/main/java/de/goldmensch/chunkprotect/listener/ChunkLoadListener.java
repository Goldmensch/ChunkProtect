package de.goldmensch.chunkprotect.listener;

import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.storage.services.DataService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.concurrent.ExecutorService;

public class ChunkLoadListener implements Listener {

    private final ExecutorService executorService;
    private final DataService dataService;

    public ChunkLoadListener(ChunkProtect protect) {
        executorService = protect.getService();
        dataService = protect.getDataService();
    }

    @EventHandler
    public void handleChunkLoad(ChunkLoadEvent event) { 
        executorService.execute(() ->
                dataService.loadChunkIfUnloaded(ChunkLocation.fromChunk(event.getChunk())));
    }

    @EventHandler
    public void handleChunkUnload(ChunkUnloadEvent event) {
        executorService.execute(() ->
                dataService.writeAndInvalidate(ChunkLocation.fromChunk(event.getChunk())));
    }
}
