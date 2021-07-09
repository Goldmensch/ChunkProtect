package de.goldmensch.chunkprotect.configuration.entities;

import java.util.Objects;

public final class EntityProtection {
    private final boolean damage;
    private final boolean interact;

    public EntityProtection(boolean damage, boolean interact) {
        this.damage = damage;
        this.interact = interact;
    }

    public boolean damage() {
        return damage;
    }

    public boolean interact() {
        return interact;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EntityProtection) obj;
        return this.damage == that.damage &&
                this.interact == that.interact;
    }

    @Override
    public int hashCode() {
        return Objects.hash(damage, interact);
    }

    @Override
    public String toString() {
        return "EntityProtection[" +
                "damage=" + damage + ", " +
                "interact=" + interact + ']';
    }

}
