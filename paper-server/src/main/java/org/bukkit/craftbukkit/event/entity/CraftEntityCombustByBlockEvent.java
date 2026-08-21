package org.bukkit.craftbukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.jspecify.annotations.Nullable;

public class CraftEntityCombustByBlockEvent extends CraftEntityCombustEvent implements EntityCombustByBlockEvent {

    private final Block combuster;

    public CraftEntityCombustByBlockEvent(final @Nullable Block combuster, final Entity combustee, final float duration) {
        super(combustee, duration);
        this.combuster = combuster;
    }

    @Override
    public @Nullable Block getCombuster() {
        return this.combuster;
    }
}
