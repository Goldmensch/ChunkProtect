package de.goldmensch.chunkprotect.configuration.protection.elements.options;

public class FromToChunkOption {
    public static final FromToChunkOption NO_PROTECTION = new FromToChunkOption(ChunkOption.NO_PROTECTION, ChunkOption.NO_PROTECTION);

    private ChunkOption fromUnclaimedInto = new ChunkOption();
    private ChunkOption fromClaimedInto = new ChunkOption();

    public ChunkOption getFromUnclaimedInto() {
        return fromUnclaimedInto;
    }

    public ChunkOption getFromClaimedInto() {
        return fromClaimedInto;
    }

    public FromToChunkOption(ChunkOption fromUnclaimedInto, ChunkOption fromClaimedInto) {
        this.fromUnclaimedInto = fromUnclaimedInto;
        this.fromClaimedInto = fromClaimedInto;
    }

    public FromToChunkOption() {
    }
}
