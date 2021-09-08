package de.goldmensch.chunkprotect.listener.protect;

import de.goldmensch.chunkprotect.ChunkProtectPlugin;

public class ProtectListeners extends InteractProtectListener {

  public ProtectListeners(ChunkProtectPlugin chunkProtectPlugin) {
    super(chunkProtectPlugin.getDataService(), chunkProtectPlugin);
  }
}
