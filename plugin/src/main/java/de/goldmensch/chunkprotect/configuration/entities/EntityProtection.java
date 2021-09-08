package de.goldmensch.chunkprotect.configuration.entities;

import de.goldmensch.chunkprotect.configuration.protection.elements.options.ChunkOption;

public final class EntityProtection {

  private final ChunkOption damage;
  private final ChunkOption playerInteract;

  public EntityProtection(ChunkOption damage, ChunkOption playerInteract) {
    this.damage = damage;
    this.playerInteract = playerInteract;
  }

  public ChunkOption getDamage() {
    return damage;
  }

  public ChunkOption getPlayerInteract() {
    return playerInteract;
  }

}
