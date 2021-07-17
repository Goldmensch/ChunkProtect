package de.goldmensch.chunkprotect.configuration.protection;

import de.goldmensch.chunkprotect.configuration.protection.elements.Block;
import de.goldmensch.chunkprotect.configuration.protection.elements.Entity;
import de.goldmensch.chunkprotect.configuration.protection.elements.Other;

public class ProtectionFile {
    private int version = 1;
    private Block block = new Block();
    private Entity entity = new Entity();
    private Other other = new Other();

    public Block getBlock() {
        return block;
    }

    public Entity getEntity() {
        return entity;
    }

    public Other getOther() {
        return other;
    }

    public int getVersion() {
        return version;
    }
}
