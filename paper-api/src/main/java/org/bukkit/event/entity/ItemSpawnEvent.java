package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Item;

/**
 * Called when an item is spawned into a world
 */
public class ItemSpawnEvent extends EntitySpawnEvent {

    @Deprecated
    public ItemSpawnEvent(final Item spawnee, final Location loc) {
        this(spawnee);
    }

    public ItemSpawnEvent(final Item spawnee) {
        super(spawnee);
    }

    @Override
    public Item getEntity() {
        return (Item) entity;
    }
}
