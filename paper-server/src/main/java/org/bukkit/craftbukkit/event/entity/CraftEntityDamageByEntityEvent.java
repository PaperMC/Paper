package org.bukkit.craftbukkit.event.entity;

import java.util.Map;
import java.util.function.Function;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CraftEntityDamageByEntityEvent extends CraftEntityDamageEvent implements EntityDamageByEntityEvent {

    private final Entity damager;
    private final boolean critical;

    public CraftEntityDamageByEntityEvent(
        final Entity damager,
        final Entity damagee,
        final DamageCause cause,
        final DamageSource damageSource,
        final Map<DamageModifier, Double> modifiers,
        final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions,
        final boolean critical
    ) {
        super(damagee, cause, damageSource, modifiers, modifierFunctions);
        this.damager = damager;
        this.critical = critical;
    }

    public CraftEntityDamageByEntityEvent(
        final net.minecraft.world.entity.Entity damager,
        final net.minecraft.world.entity.Entity damagee,
        final DamageCause cause,
        final net.minecraft.world.damagesource.DamageSource damageSource,
        final Map<DamageModifier, Double> modifiers,
        final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions
    ) {
        this(
            damager.getBukkitEntity(),
            damagee.getBukkitEntity(),
            cause,
            new CraftDamageSource(damageSource),
            modifiers,
            modifierFunctions,
            damageSource.isCritical()
        );
    }

    @Override
    public Entity getDamager() {
        return this.damager;
    }

    @Override
    public boolean isCritical() {
        return this.critical;
    }
}
