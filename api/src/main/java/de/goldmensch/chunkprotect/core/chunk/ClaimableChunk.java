package de.goldmensch.chunkprotect.core.chunk;

import de.goldmensch.chunkprotect.ChunkLocation;
import java.util.function.Consumer;

public final class ClaimableChunk {

  private final ClaimedChunk chunk;
  private final ChunkLocation location;

  public ClaimableChunk(ClaimedChunk chunk, ChunkLocation location) {
    this.chunk = chunk;
    this.location = location;
  }

  public static ClaimableChunk forceClaimed(ChunkLocation location) {
    return new ClaimableChunk(ClaimedChunk.forceClaimed(location), location);
  }

  public boolean isClaimed() {
    return chunk != null;
  }

  public void ifClaimed(Consumer<ClaimedChunk> function) {
    if (isClaimed()) {
      function.accept(chunk);
    }
  }

  public void ifClaimedOr(Consumer<ClaimedChunk> function, Runnable or) {
    if (isClaimed()) {
      function.accept(chunk);
    } else {
      or.run();
    }
  }

  public ClaimedChunk getChunk() {
    return chunk;
  }

  public ChunkLocation getLocation() {
    return location;
  }

  @Override
  public String toString() {
    return "ClaimableChunk{" +
        "chunk=" + chunk +
        ", location=" + location +
        '}';
  }
}
