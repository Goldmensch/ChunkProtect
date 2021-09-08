package de.goldmensch.chunkprotect.configuration.protection.elements.options;

public class ChunkOption {

  public static final ChunkOption NO_PROTECTION = new ChunkOption(false, false);

  private boolean unclaimed;
  private boolean claimed = true;

  public ChunkOption(boolean unclaimed, boolean claimed) {
    this.unclaimed = unclaimed;
    this.claimed = claimed;
  }

  public ChunkOption() {
  }

  public boolean isUnclaimed() {
    return unclaimed;
  }

  public boolean isClaimed() {
    return claimed;
  }
}
