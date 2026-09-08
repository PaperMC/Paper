package org.bukkit.craftbukkit.event.data;

import com.google.common.base.Preconditions;
import org.bukkit.entity.EntityType;
import org.checkerframework.checker.index.qual.NonNegative;

import static io.papermc.paper.util.BoundChecker.requireNonNegative;

public final class TransientEggInfo {

    private boolean hatching;
    private EntityType type;
    private int numHatches;

    public TransientEggInfo(final boolean hatching, final EntityType type, final int numHatches) {
        this.hatching = hatching;
        this.type = type;
        this.numHatches = numHatches;
    }

    public boolean isHatching() {
        return this.hatching;
    }

    public TransientEggInfo setHatching(final boolean hatching) {
        this.hatching = hatching;
        return this;
    }

    public EntityType getType() {
        return this.type;
    }

    public TransientEggInfo setType(final EntityType type) {
        Preconditions.checkArgument(type.isSpawnable(), "Can't spawn that entity type from an egg!");
        this.type = type;
        return this;
    }

    public @NonNegative int getNumHatches() {
        if (!this.hatching) {
            this.numHatches = 0;
        }
        return this.numHatches;
    }

    public TransientEggInfo setNumHatches(final @NonNegative int numHatches) {
        this.numHatches = requireNonNegative(numHatches, "numHatches");
        return this;
    }
}
