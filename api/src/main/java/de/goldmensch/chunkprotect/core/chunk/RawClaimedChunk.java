package de.goldmensch.chunkprotect.core.chunk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import de.goldmensch.chunkprotect.ChunkLocation;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class RawClaimedChunk {

  private final ChunkLocation location;
  private final UUID holderUUID;
  private final Set<UUID> trustedPlayer;

  @JsonCreator
  @ApiStatus.AvailableSince("1.0")
  public RawClaimedChunk(@JsonProperty("location") @NotNull ChunkLocation location,
                         @JsonProperty("holderUUID") @NotNull UUID holderUUID,
                         @JsonProperty("trustedPlayer") Set<UUID> trustedPlayer) {

    Objects.requireNonNull(location);
    Objects.requireNonNull(holderUUID);

    this.location = location;
    this.holderUUID = holderUUID;
    this.trustedPlayer = Objects.requireNonNullElse(Sets.newConcurrentHashSet(trustedPlayer),
        Sets.newConcurrentHashSet());
  }

  public @NotNull ChunkLocation getLocation() {
    return location;
  }

  public @NotNull Set<UUID> getTrustedPlayer() {
    return trustedPlayer;
  }

  public @NotNull UUID getHolderUUID() {
    return holderUUID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    var that = (RawClaimedChunk) o;
    return Objects.equals(location, that.location) &&
        Objects.equals(holderUUID, that.holderUUID) &&
        Objects.equals(trustedPlayer, that.trustedPlayer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(location, holderUUID, trustedPlayer);
  }

  @Override
  public String toString() {
    return "ClaimedChunk[" +
        "location=" + location + ", " +
        "holder=" + holderUUID + ']';
  }
}
