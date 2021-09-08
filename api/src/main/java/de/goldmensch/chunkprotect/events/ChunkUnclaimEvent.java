package de.goldmensch.chunkprotect.events;

import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ChunkUnclaimEvent extends ChunkEvent {

  private static final HandlerList handlerList = new HandlerList();
  private final ClaimedChunk claimedChunk;

  public ChunkUnclaimEvent(ClaimedChunk claimedChunk) {
    this.claimedChunk = claimedChunk;
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }

  public ClaimedChunk getClaimedChunk() {
    return claimedChunk;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return handlerList;
  }
}
