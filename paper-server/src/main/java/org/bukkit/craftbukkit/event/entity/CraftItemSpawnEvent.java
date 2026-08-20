package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Item;
import org.bukkit.event.entity.ItemSpawnEvent;

public class CraftItemSpawnEvent extends CraftEntitySpawnEvent implements ItemSpawnEvent {

    public CraftItemSpawnEvent(final Item spawnee) {
        super(spawnee);
    }

    @Override
    public Item getEntity() {
        return (Item) this.entity;
    }
}
