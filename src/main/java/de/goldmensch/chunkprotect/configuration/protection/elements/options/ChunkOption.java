package de.goldmensch.chunkprotect.configuration.protection.elements.options;

public class ChunkOption {
    private boolean unclaimed = false;
    private boolean claimed = true;

    public ChunkOption(boolean unclaimed, boolean claimed) {
        this.unclaimed = unclaimed;
        this.claimed = claimed;
    }

    public ChunkOption() {}

    public boolean isUnclaimed() {
        return unclaimed;
    }

    public boolean isClaimed() {
        return claimed;
    }
}
