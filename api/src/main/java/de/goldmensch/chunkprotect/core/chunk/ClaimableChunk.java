package de.goldmensch.chunkprotect.core.chunk;

import de.goldmensch.chunkprotect.Checks;
import de.goldmensch.chunkprotect.ChunkLocation;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ClaimableChunk {

  private final ClaimedChunk chunk;
  private final ChunkLocation location;

  public ClaimableChunk(@Nullable ClaimedChunk chunk, @NotNull ChunkLocation location) {
    this.chunk = chunk;
    this.location = Checks.notNull(location, "location");
  }

  public static ClaimableChunk forceClaimed(ChunkLocation location) {
    return new ClaimableChunk(ClaimedChunk.forceClaimed(location), location);
  }

  public boolean isClaimed() {
    return chunk != null;
  }

  public boolean ifClaimed(Consumer<ClaimedChunk> function) {
    if (isClaimed()) {
      function.accept(chunk);
      return true;
    }
    return false;
  }

  public boolean ifClaimedOr(Consumer<ClaimedChunk> function, Runnable or) {
    if (isClaimed()) {
      function.accept(chunk);
      return true;
    }
    or.run();
    return false;
  }

  @NotNull
  public ClaimedChunk getChunk() {
    return chunk;
  }

  @NotNull
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
