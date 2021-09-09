package de.goldmensch.chunkprotect.core.chunk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import de.goldmensch.chunkprotect.ChunkLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class RawClaimedChunk {

  private final ChunkLocation location;
  private final UUID holderUUID;
  private final Set<UUID> trustedPlayer;

  @JsonCreator
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

  @NotNull
  public ChunkLocation getLocation() {
    return location;
  }

  @NotNull
  public Set<UUID> getTrustedPlayer() {
    return trustedPlayer;
  }

  @NotNull
  public UUID getHolderUUID() {
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
    return "RawClaimedChunk{" +
            "location=" + location +
            ", holderUUID=" + holderUUID +
            ", trustedPlayer=" + trustedPlayer +
            '}';
  }
}
