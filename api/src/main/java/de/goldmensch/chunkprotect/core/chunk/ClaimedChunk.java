package de.goldmensch.chunkprotect.core.chunk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.goldmensch.chunkprotect.ChunkLocation;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public final class ClaimedChunk extends RawClaimedChunk {

  private static final ChunkHolder FORCE_HOLDER = new ChunkHolder("chunk_forceClaimed",
          UUID.fromString("8534a7ba-9aa6-4bbe-a93a-3632a9781f53"),
          false,
          true);

  private final ChunkHolder holder;
  private final boolean forceClaimed;

  public ClaimedChunk(RawClaimedChunk chunk, ChunkHolder holder) {
    super(chunk.getLocation(), chunk.getHolderUUID(), chunk.getTrustedPlayer());
    this.holder = holder;
    forceClaimed = false;
  }

  private ClaimedChunk(RawClaimedChunk chunk, ChunkHolder holder, boolean forceClaimed) {
    super(chunk.getLocation(), chunk.getHolderUUID(), chunk.getTrustedPlayer());
    this.holder = holder;
    this.forceClaimed = forceClaimed;
  }

  public static ClaimedChunk forceClaimed(ChunkLocation location) {
    return new ClaimedChunk(
            new RawClaimedChunk(location, FORCE_HOLDER.getUuid(), new HashSet<>()),
            FORCE_HOLDER,
            true
    );
  }

  @NotNull
  @JsonIgnore
  public ChunkHolder getHolder() {
    return holder;
  }

  @JsonIgnore
  public boolean isForceClaimed() {
    return forceClaimed;
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
    var that = (ClaimedChunk) o;
    return forceClaimed == that.forceClaimed &&
            Objects.equals(holder, that.holder);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), holder, forceClaimed);
  }

  @Override
  public String toString() {
    return "ClaimedChunk{" +
            "holder=" + holder +
            ", forceClaimed=" + forceClaimed +
            '}';
  }
}
