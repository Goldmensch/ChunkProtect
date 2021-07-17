package de.goldmensch.chunkprotect.configuration.protection.elements;

import de.goldmensch.chunkprotect.configuration.protection.elements.options.ChunkOption;

public class Other {
    private ChunkOption bucketFill = new ChunkOption();
    private ChunkOption bucketEmpty = new ChunkOption();

    public ChunkOption getBucketFill() {
        return bucketFill;
    }

    public ChunkOption getBucketEmpty() {
        return bucketEmpty;
    }
}
