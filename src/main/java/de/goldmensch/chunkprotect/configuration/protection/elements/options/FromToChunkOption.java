package de.goldmensch.chunkprotect.configuration.protection.elements.options;

public class FromToChunkOption {
    private ChunkOption fromUnclaimedInto = new ChunkOption();
    private ChunkOption fromClaimedInto = new ChunkOption();

    public ChunkOption getFromUnclaimedInto() {
        return fromUnclaimedInto;
    }

    public ChunkOption getFromClaimedInto() {
        return fromClaimedInto;
    }
}
