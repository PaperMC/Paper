package org.bukkit.event.entity;

import com.google.common.base.Function;
import java.util.Map;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity is damaged by an entity
 */
public class EntityDamageByEntityEvent extends EntityDamageEvent {
    private final Entity damager;

    public EntityDamageByEntityEvent(@NotNull final Entity damager, @NotNull final Entity damagee, @NotNull final DamageCause cause, @NotNull final DamageSource damageSource, final double damage) {
        super(damagee, cause, damageSource, damage);
        this.damager = damager;
    }

    public EntityDamageByEntityEvent(@NotNull final Entity damager, @NotNull final Entity damagee, @NotNull final DamageCause cause, @NotNull final DamageSource damageSource, @NotNull final Map<DamageModifier, Double> modifiers, @NotNull final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
        super(damagee, cause, damageSource, modifiers, modifierFunctions);
        this.damager = damager;
    }

    /**
     * Returns the entity that damaged the defender.
     *
     * @return Entity that damaged the defender.
     */
    @NotNull
    public Entity getDamager() {
        return damager;
    }
}
