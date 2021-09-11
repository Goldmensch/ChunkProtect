package de.goldmensch.chunkprotect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

public final class ChunkLocation {

  private final int x;
  private final int z;
  private final String world;

  @JsonCreator
  public ChunkLocation(@JsonProperty("x") int x,
                       @JsonProperty("z") int z,
                       @JsonProperty("world") @NotNull String world) {
    this.x = x;
    this.z = z;
    this.world = Checks.notNull(world, "world");
  }

  public static ChunkLocation fromChunk(@NotNull Chunk chunk) {
    Checks.notNull(chunk, "chunk");
    return new ChunkLocation(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
  }

  public int getX() {
    return x;
  }

  public int getZ() {
    return z;
  }

  @NotNull
  public String getWorld() {
    return world;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var chunkLocation = (ChunkLocation) obj;
    return this.x == chunkLocation.x &&
        this.z == chunkLocation.z &&
        this.world.equals(chunkLocation.world);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, z, world);
  }

  @Override
  public String toString() {
    return "ChunkLocation[" +
        "x=" + x + ", " +
        "y=" + z + ", " +
        "world= " + world + ']';
  }

}
