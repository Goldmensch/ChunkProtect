package de.goldmensch.chunkprotect;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChunkProtectionBypass implements ProtectionBypass {

  private final Set<UUID> players = new HashSet<>();

  public boolean toggle(UUID uuid) {
    if (players.contains(uuid)) {
      players.remove(uuid);
    } else {
      players.add(uuid);
    }
    return players.contains(uuid);
  }

  public boolean hasBypass(UUID uuid) {
    return players.contains(uuid);
  }

  public boolean remove(UUID uuid) {
    return players.remove(uuid);
  }

  @Override
  public boolean add(UUID uuid) {
    return players.add(uuid);
  }
}
