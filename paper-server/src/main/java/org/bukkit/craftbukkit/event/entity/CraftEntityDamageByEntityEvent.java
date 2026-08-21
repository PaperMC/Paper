package org.bukkit.craftbukkit.event.entity;

import com.google.common.base.Function;
import java.util.Map;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CraftEntityDamageByEntityEvent extends CraftEntityDamageEvent implements EntityDamageByEntityEvent {

    private final Entity damager;
    private final boolean critical;

    public CraftEntityDamageByEntityEvent(final Entity damager, final Entity damagee, final DamageCause cause, final DamageSource damageSource, final Map<DamageModifier, Double> modifiers, final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions, final boolean critical) {
        super(damagee, cause, damageSource, modifiers, modifierFunctions);
        this.damager = damager;
        this.critical = critical;
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
