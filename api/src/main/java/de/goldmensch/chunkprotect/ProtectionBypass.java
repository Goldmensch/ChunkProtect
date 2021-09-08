package de.goldmensch.chunkprotect;

import java.util.UUID;

public interface ProtectionBypass {

  boolean toggle(UUID uuid);

  boolean remove(UUID uuid);

  boolean add(UUID uuid);

  boolean hasBypass(UUID uuid);
}
