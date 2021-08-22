package de.goldmensch.chunkprotect.listener.protect;

import de.goldmensch.chunkprotect.core.ChunkProtect;

public class ProtectListeners extends InteractProtectListener {
    public ProtectListeners(ChunkProtect chunkProtect) {
        super(chunkProtect.getDataService(), chunkProtect);
    }
}
