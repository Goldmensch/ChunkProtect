package de.goldmensch.chunkprotect.events;

import de.goldmensch.chunkprotect.ChunkLocation;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ChunkClaimEvent extends ChunkEvent {

  private static final HandlerList handlerList = new HandlerList();
  private final ChunkHolder holder;
  private final ChunkLocation location;

  public ChunkClaimEvent(ChunkHolder holder, ChunkLocation location) {
    this.holder = holder;
    this.location = location;
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }

  public ChunkHolder getHolder() {
    return holder;
  }

  public ChunkLocation getLocation() {
    return location;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return handlerList;
  }
}
