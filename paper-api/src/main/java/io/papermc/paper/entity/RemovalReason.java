package io.papermc.paper.entity;

import org.bukkit.entity.Entity;

/**
 * Represents the reason an entity was removed.
 *
 * @see Entity#getRemovalReason()
 */
public enum RemovalReason {
    // Start generate - RemovalReason
    KILLED(true, false),
    DISCARDED(true, false),
    UNLOADED_TO_CHUNK(false, true),
    UNLOADED_WITH_PLAYER(false, false),
    CHANGED_DIMENSION(false, false);
    // End generate - RemovalReason

    private final boolean destroy;
    private final boolean save;

    RemovalReason(final boolean destroy, final boolean save) {
        this.destroy = destroy;
        this.save = save;
    }

    /**
     * {@return whether the entity should be destroyed after being removed}
     */
    public boolean shouldDestroy() {
        return this.destroy;
    }

    /**
     * {@return whether the entity should be saved after being removed}
     */
    public boolean shouldSave() {
        return this.save;
    }
}
