package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityCombustByEntityEvent;

public class CraftEntityCombustByEntityEvent extends CraftEntityCombustEvent implements EntityCombustByEntityEvent {

    private final Entity combuster;

    public CraftEntityCombustByEntityEvent(final Entity combuster, final Entity combustee, final float duration) {
        super(combustee, duration);
        this.combuster = combuster;
    }

    public CraftEntityCombustByEntityEvent(
        final net.minecraft.world.entity.Entity combuster,
        final net.minecraft.world.entity.Entity combustee,
        final float duration
    ) {
        this(combuster.getBukkitEntity(), combustee.getBukkitEntity(), duration);
    }

    @Override
    public Entity getCombuster() {
        return this.combuster;
    }
}
