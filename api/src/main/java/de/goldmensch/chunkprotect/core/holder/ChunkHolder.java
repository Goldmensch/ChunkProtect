package de.goldmensch.chunkprotect.core.holder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.goldmensch.chunkprotect.ChunkLocation;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ChunkHolder {

  private String name; // no default value
  private UUID uuid; // no default value
  private boolean isPlayer; // no default value
  private Set<UUID> trustedAllChunks = new HashSet<>();
  private Set<ChunkLocation> claimedChunks = ConcurrentHashMap.newKeySet();
  @JsonIgnore
  private boolean fallback;

  public ChunkHolder(String name, UUID uuid, boolean isPlayer) {
    this.name = name;
    this.uuid = uuid;
    this.isPlayer = isPlayer;
  }

  public ChunkHolder(String name, UUID uuid, boolean isPlayer, boolean fallback) {
    this.name = name;
    this.uuid = uuid;
    this.isPlayer = isPlayer;
    this.fallback = fallback;
  }

  public ChunkHolder() {
  } // for json

  public static ChunkHolder fallback(UUID uuid) {
    return new ChunkHolder(uuid.toString(), uuid, false, true);
  }

  // Getter/Setter
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public boolean isPlayer() {
    return isPlayer;
  }

  public void setPlayer(boolean player) {
    isPlayer = player;
  }

  public Set<UUID> getTrustedAllChunks() {
    return trustedAllChunks;
  }

  public void setTrustedAllChunks(Set<UUID> trustedAllChunks) {
    this.trustedAllChunks = trustedAllChunks;
  }

  public Set<ChunkLocation> getClaimedChunks() {
    return claimedChunks;
  }

  public void setClaimedChunks(Set<ChunkLocation> claimedChunks) {
    this.claimedChunks = claimedChunks;
  }

  public boolean isFallback() {
    return fallback;
  }

  public void setFallback(boolean fallback) {
    this.fallback = fallback;
  }

  public boolean isNoFallback() {
    return !fallback;
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
    ChunkHolder that = (ChunkHolder) o;
    return isPlayer == that.isPlayer && fallback == that.fallback && Objects.equals(name, that.name)
        &&
        Objects.equals(uuid, that.uuid) && Objects.equals(trustedAllChunks, that.trustedAllChunks)
        &&
        Objects.equals(claimedChunks, that.claimedChunks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, uuid, isPlayer, trustedAllChunks, claimedChunks);
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
