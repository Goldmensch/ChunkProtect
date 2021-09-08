package de.goldmensch.chunkprotect;

import org.bukkit.Chunk;

import java.util.Objects;

public final class ChunkLocation {
    private int x; // no default value
    private int z; // no default value
    private String world;

    public ChunkLocation(int x, int z, String world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    public ChunkLocation() {
    } // for json

    public static ChunkLocation fromChunk(Chunk chunk) {
        return new ChunkLocation(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
    }

    //Getter/Setter
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    // Object stuff
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var chunkLocation = (ChunkLocation) obj;
        return this.x == chunkLocation.x &&
                this.z == chunkLocation.z &&
                this.world.equals(chunkLocation.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    @Override
    public String toString() {
        return "ChunkLocation[" +
                "x=" + x + ", " +
                "y=" + z + ", " +
                "world= " + world + ']';
    }

}
