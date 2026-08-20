package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.EntityBlockFormEvent;

public class CraftEntityBlockFormEvent extends CraftBlockFormEvent implements EntityBlockFormEvent {

    private final Entity entity;

    public CraftEntityBlockFormEvent(final Entity entity, final Block block, final BlockState newState) {
        super(block, newState);
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }
}
