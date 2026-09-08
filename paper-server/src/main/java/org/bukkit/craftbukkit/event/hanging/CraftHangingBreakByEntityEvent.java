package org.bukkit.craftbukkit.event.hanging;

import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class CraftHangingBreakByEntityEvent extends CraftHangingBreakEvent implements HangingBreakByEntityEvent {

    private final Entity remover;
    private final DamageSource damageSource;

    public CraftHangingBreakByEntityEvent(final Hanging hanging, final Entity remover, final DamageSource damageSource, final RemoveCause cause) {
        super(hanging, cause);
        this.remover = remover;
        this.damageSource = damageSource;
    }

    public CraftHangingBreakByEntityEvent(
        final net.minecraft.world.entity.Entity hanging,
        final net.minecraft.world.entity.Entity remover,
        final net.minecraft.world.damagesource.DamageSource damageSource,
        final RemoveCause cause
    ) {
        this((Hanging) hanging.getBukkitEntity(), remover.getBukkitEntity(), damageSource, cause);
    }

    public CraftHangingBreakByEntityEvent(
        final Hanging hanging,
        final Entity remover,
        final net.minecraft.world.damagesource.DamageSource damageSource,
        final RemoveCause cause
    ) {
        this(hanging, remover, new CraftDamageSource(damageSource), cause);
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
