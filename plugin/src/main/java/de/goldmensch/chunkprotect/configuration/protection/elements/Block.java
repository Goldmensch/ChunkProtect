package de.goldmensch.chunkprotect.configuration.protection.elements;

import de.goldmensch.chunkprotect.configuration.protection.elements.options.ChunkOption;
import de.goldmensch.chunkprotect.configuration.protection.elements.options.FromToChunkOption;

public class Block {

  private ChunkOption breakBlock = new ChunkOption();
  private ChunkOption placeBlock = new ChunkOption();
  private FromToChunkOption pistonExtend = new FromToChunkOption();
  private FromToChunkOption pistonRetract = new FromToChunkOption();
  private FromToChunkOption waterFlow = new FromToChunkOption();
  private FromToChunkOption lavaFlow = new FromToChunkOption();
  private FromToChunkOption dragonEggTeleport = new FromToChunkOption();
  private ChunkOption tntPrime = new ChunkOption();
  private ChunkOption blockBreakByExplosion = new ChunkOption();
  private ChunkOption playerInteract = new ChunkOption();

  public ChunkOption getBreakBlock() {
    return breakBlock;
  }

  public ChunkOption getPlaceBlock() {
    return placeBlock;
  }

  public FromToChunkOption getPistonExtend() {
    return pistonExtend;
  }

  public FromToChunkOption getPistonRetract() {
    return pistonRetract;
  }

  public FromToChunkOption getWaterFlow() {
    return waterFlow;
  }

  public FromToChunkOption getLavaFlow() {
    return lavaFlow;
  }

  public FromToChunkOption getDragonEggTeleport() {
    return dragonEggTeleport;
  }

  public ChunkOption getTntPrime() {
    return tntPrime;
  }

  public ChunkOption getBlockBreakByExplosion() {
    return blockBreakByExplosion;
  }

  public ChunkOption getPlayerInteract() {
    return playerInteract;
  }
}

