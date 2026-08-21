package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityCombustByEntityEvent;

public class CraftEntityCombustByEntityEvent extends CraftEntityCombustEvent implements EntityCombustByEntityEvent {

    private final Entity combuster;

    public CraftEntityCombustByEntityEvent(final Entity combuster, final Entity combustee, final float duration) {
        super(combustee, duration);
        this.combuster = combuster;
    }

    @Override
    public Entity getCombuster() {
        return this.combuster;
    }
}
