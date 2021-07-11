package de.goldmensch.chunkprotect.core.holder;

import com.jsoniter.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class ChunkHolder {
    private String name; // no default value
    private  UUID uuid; // no default value
    private int claimAmount = 0;
    private boolean isPlayer; // no default value
    private Set<UUID> trustedAllChunks = new HashSet<>();
    @JsonIgnore
    private boolean fallback = false;

    public ChunkHolder(String name, UUID uuid, int claimAmount, boolean isPlayer) {
        this.name = name;
        this.uuid = uuid;
        this.claimAmount = claimAmount;
        this.isPlayer = isPlayer;
    }

    public ChunkHolder(String name, UUID uuid, int claimAmount, boolean isPlayer, boolean fallback) {
        this.name = name;
        this.uuid = uuid;
        this.claimAmount = claimAmount;
        this.isPlayer = isPlayer;
        this.fallback = fallback;
    }

    public static ChunkHolder fallback(UUID uuid) {
        return new ChunkHolder(uuid.toString(), uuid, 0, false, true);
    }

    public ChunkHolder() {} // for json

    // Getter/Setter
    public Set<UUID> getTrustedAllChunks() {
        return trustedAllChunks;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getClaimAmount() {
        return claimAmount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClaimAmount(int claimAmount) {
        this.claimAmount = claimAmount;
    }

    public void setPlayer(boolean player) {
        isPlayer = player;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public boolean isNoFallback() {
        return !fallback;
    }

    // object stuff
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ChunkHolder) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.uuid, that.uuid) &&
                this.claimAmount == that.claimAmount &&
                this.isPlayer == that.isPlayer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uuid, claimAmount, isPlayer);
    }

    @Override
    public String toString() {
        return "ChunkHolder[" +
                "name=" + name + ", " +
                "uuid=" + uuid + ", " +
                "claimAmount=" + claimAmount + ", " +
                "isPlayer=" + isPlayer + ']';
    }

}
