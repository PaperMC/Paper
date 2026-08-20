package org.bukkit.event.entity;

import org.bukkit.entity.Item;

/**
 * Called when an item is spawned into a world
 */
public interface ItemSpawnEvent extends EntitySpawnEvent {

    @Override
    Item getEntity();
}
