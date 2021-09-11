package de.goldmensch.chunkprotect.core.holder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import de.goldmensch.chunkprotect.Checks;
import de.goldmensch.chunkprotect.ChunkLocation;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public final class ChunkHolder {

  private final UUID uuid;
  private final boolean isPlayer;
  private final Set<UUID> trustedAllChunks = Sets.newConcurrentHashSet();
  private final Set<ChunkLocation> claimedChunks = Sets.newConcurrentHashSet();
  private final boolean fallback;
  private String name;

  @JsonCreator
  public ChunkHolder(@JsonProperty("name") @NotNull String name,
                     @JsonProperty("uuid") @NotNull UUID uuid,
                     @JsonProperty("isPlayer") boolean isPlayer) {

    this(name, uuid, isPlayer, false);
  }

  public ChunkHolder(@NotNull String name, @NotNull UUID uuid, boolean isPlayer, boolean fallback) {
    this.name = Checks.notNull(name, "name");
    this.uuid = Checks.notNull(uuid, "uuid");
    this.isPlayer = isPlayer;
    this.fallback = fallback;
  }

  public static ChunkHolder fallback(UUID uuid) {
    return new ChunkHolder(uuid.toString(), uuid, false, true);
  }

  public boolean updateName(@NotNull String newName) {
    Objects.requireNonNull(newName);
    var updated = !newName.equals(name);
    if (updated) {
      name = newName;
    }
    return updated;
  }

  @NotNull
  public String getName() {
    return name;
  }

  @NotNull
  public UUID getUuid() {
    return uuid;
  }

  @JsonProperty("isPlayer")
  public boolean isPlayer() {
    return isPlayer;
  }

  @JsonIgnore
  public boolean isFallback() {
    return fallback;
  }

  @JsonIgnore
  public boolean noFallback() {
    return !fallback;
  }

  @NotNull
  public Set<UUID> getTrustedAllChunks() {
    return trustedAllChunks;
  }

  @NotNull
  public Set<ChunkLocation> getClaimedChunks() {
    return claimedChunks;
  }

  @JsonIgnore
  public int getClaimAmount() {
    return claimedChunks.size();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    var that = (ChunkHolder) o;
    return isPlayer == that.isPlayer &&
        fallback == that.fallback &&
        Objects.equals(name, that.name) &&
        Objects.equals(uuid, that.uuid) &&
        Objects.equals(trustedAllChunks, that.trustedAllChunks) &&
        Objects.equals(claimedChunks, that.claimedChunks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, uuid, isPlayer, trustedAllChunks, claimedChunks, fallback);
  }

  @Override
  public String toString() {
    return "ChunkHolder{" +
        "name='" + name + '\'' +
        ", uuid=" + uuid +
        ", isPlayer=" + isPlayer +
        ", trustedAllChunks=" + trustedAllChunks +
        ", claimedChunks=" + claimedChunks +
        ", fallback=" + fallback +
        '}';
  }
}
