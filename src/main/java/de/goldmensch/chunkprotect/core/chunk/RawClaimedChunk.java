package de.goldmensch.chunkprotect.core.chunk;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class RawClaimedChunk {
    private ChunkLocation location; // no default value
    private UUID holderUUID; // no default value
    private Set<UUID> trustedPlayer = new HashSet<>(); //default empty list

    public RawClaimedChunk(ChunkLocation location, UUID holderUUID, Set<UUID> trustedPlayer) {
        this.location = location;
        this.holderUUID = holderUUID;
        this.trustedPlayer = trustedPlayer;
    }

    public RawClaimedChunk() {
    }

    // Getter/Setter
    public ChunkLocation getLocation() {
        return location;
    }

    public void setLocation(ChunkLocation location) {
        this.location = location;
    }

    public Set<UUID> getTrustedPlayer() {
        return trustedPlayer;
    }

    public void setTrustedPlayer(Set<UUID> trustedPlayer) {
        this.trustedPlayer = trustedPlayer;
    }

    public UUID getHolderUUID() {
        return holderUUID;
    }

    public void setHolderUUID(UUID holder) {
        this.holderUUID = holder;
    }

    //Object stuff
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var rawClaimedChunk = (RawClaimedChunk) obj;
        return Objects.equals(this.location, rawClaimedChunk.location) &&
                Objects.equals(this.holderUUID, rawClaimedChunk.holderUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, holderUUID);
    }

    @Override
    public String toString() {
        return "ClaimedChunk[" +
                "location=" + location + ", " +
                "holder=" + holderUUID + ']';
    }


}
