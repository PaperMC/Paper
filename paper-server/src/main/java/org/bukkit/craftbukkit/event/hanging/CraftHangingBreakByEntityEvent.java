package org.bukkit.craftbukkit.event.hanging;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class CraftHangingBreakByEntityEvent extends CraftHangingBreakEvent implements HangingBreakByEntityEvent {

    private final Entity remover;
    private final DamageSource damageSource;

    public CraftHangingBreakByEntityEvent(final Hanging hanging, final Entity remover, final DamageSource damageSource) {
        this(hanging, remover, damageSource, RemoveCause.ENTITY);
    }

    public CraftHangingBreakByEntityEvent(final Hanging hanging, final Entity remover, final DamageSource damageSource, final RemoveCause cause) {
        super(hanging, cause);
        this.remover = remover;
        this.damageSource = damageSource;
    }

    @Override
    public Entity getRemover() {
        return this.remover;
    }

    @Override
    public DamageSource getDamageSource() {
        return this.damageSource;
    }
}
