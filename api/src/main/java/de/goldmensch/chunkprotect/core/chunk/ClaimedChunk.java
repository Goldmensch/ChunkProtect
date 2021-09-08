package de.goldmensch.chunkprotect.core.chunk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.goldmensch.chunkprotect.ChunkLocation;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public final class ClaimedChunk extends RawClaimedChunk {

  private static final ChunkHolder FORCE_HOLDER = new ChunkHolder("chunk_forceClaimed",
      UUID.fromString("8534a7ba-9aa6-4bbe-a93a-3632a9781f53"),
      false,
      true);

  private final ChunkHolder holder;
  private boolean forceClaimed;

  public ClaimedChunk(RawClaimedChunk chunk, ChunkHolder holder) {
    super(chunk.getLocation(), chunk.getHolderUUID(), chunk.getTrustedPlayer());
    this.holder = holder;
  }

  public static ClaimedChunk forceClaimed(ChunkLocation location) {
    return new ClaimedChunk(
        new RawClaimedChunk(location, FORCE_HOLDER.getUuid(), new HashSet<>()),
        FORCE_HOLDER
    ).forceClaimed();
  }

  @JsonIgnore
  public ChunkHolder getHolder() {
    return holder;
  }

  @JsonIgnore
  public boolean notForceClaimed() {
    return !forceClaimed;
  }

  private ClaimedChunk forceClaimed() {
    forceClaimed = true;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    var claimedChunk = (ClaimedChunk) o;
    return forceClaimed == claimedChunk.forceClaimed && Objects.equals(holder, claimedChunk.holder);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), holder, forceClaimed);
  }
}
