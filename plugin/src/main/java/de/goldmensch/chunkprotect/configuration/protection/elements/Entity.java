package de.goldmensch.chunkprotect.configuration.protection.elements;

import de.goldmensch.chunkprotect.configuration.protection.elements.options.ChunkOption;

public class Entity {

  private ChunkOption damage = new ChunkOption();
  private ChunkOption playerInteract = new ChunkOption();

  public ChunkOption getDamage() {
    return damage;
  }

  public ChunkOption getPlayerInteract() {
    return playerInteract;
  }
}
