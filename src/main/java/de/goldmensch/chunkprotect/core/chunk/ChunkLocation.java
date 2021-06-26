package de.goldmensch.chunkprotect.core.chunk;

import org.bukkit.Chunk;

import java.util.Objects;

public final class ChunkLocation {
    private int x; // no default value
    private int z; // no default value

    public ChunkLocation(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public ChunkLocation() {} // for json

    public static ChunkLocation fromChunk(Chunk chunk) {
        return new ChunkLocation(chunk.getX(), chunk.getZ());
    }

    //Getter/Setter
    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setZ(int z) {
        this.z = z;
    }

    // Object stuff
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ChunkLocation) obj;
        return this.x == that.x &&
                this.z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    @Override
    public String toString() {
        return "ChunkLocation[" +
                "x=" + x + ", " +
                "y=" + z + ']';
    }

}
