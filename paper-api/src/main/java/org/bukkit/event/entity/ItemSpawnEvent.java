package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an item is spawned into a world
 */
public class ItemSpawnEvent extends EntitySpawnEvent {

    @ApiStatus.Internal
    @Deprecated(since = "1.13.2", forRemoval = true)
    public ItemSpawnEvent(@NotNull final Item spawnee, final Location loc) {
        this(spawnee);
    }

    @ApiStatus.Internal
    public ItemSpawnEvent(@NotNull final Item spawnee) {
        super(spawnee);
    }

    @NotNull
    @Override
    public Item getEntity() {
        return (Item) this.entity;
    }
}
