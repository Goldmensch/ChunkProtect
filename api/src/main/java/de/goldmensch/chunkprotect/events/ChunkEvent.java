package de.goldmensch.chunkprotect.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class ChunkEvent extends Event implements Cancellable {

  private boolean isCancelled;

  @Override
  public boolean isCancelled() {
    return isCancelled;
  }

  @Override
  public void setCancelled(boolean cancel) {
    isCancelled = cancel;
  }
}
