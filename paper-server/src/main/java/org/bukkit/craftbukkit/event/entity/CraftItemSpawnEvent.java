package org.bukkit.craftbukkit.event.entity;

import net.minecraft.world.entity.item.ItemEntity;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.ItemSpawnEvent;

public class CraftItemSpawnEvent extends CraftEntitySpawnEvent implements ItemSpawnEvent {

    public CraftItemSpawnEvent(final ItemEntity item) {
        super(item);
    }

    @Override
    public Item getEntity() {
        return (Item) this.entity;
    }
}
