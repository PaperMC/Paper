package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an item is spawned into a world
 *
 * @since 1.0.0 R1
 */
public class ItemSpawnEvent extends EntitySpawnEvent {

    @Deprecated(since = "1.13.2")
    public ItemSpawnEvent(@NotNull final Item spawnee, final Location loc) {
        this(spawnee);
    }

    public ItemSpawnEvent(@NotNull final Item spawnee) {
        super(spawnee);
    }

    /**
     * @since 1.1.0 R5
     */
    @NotNull
    @Override
    public Item getEntity() {
        return (Item) entity;
    }
}
