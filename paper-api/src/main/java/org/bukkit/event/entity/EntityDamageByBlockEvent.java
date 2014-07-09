package org.bukkit.event.entity;

import java.util.Map;

import com.google.common.base.Function;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

/**
 * Called when an entity is damaged by a block
 */
public class EntityDamageByBlockEvent extends EntityDamageEvent {
    private final Block damager;

    @Deprecated
    public EntityDamageByBlockEvent(final Block damager, final Entity damagee, final DamageCause cause, final int damage) {
        this(damager, damagee, cause, (double) damage);
    }

    @Deprecated
    public EntityDamageByBlockEvent(final Block damager, final Entity damagee, final DamageCause cause, final double damage) {
        super(damagee, cause, damage);
        this.damager = damager;
    }

    public EntityDamageByBlockEvent(final Block damager, final Entity damagee, final DamageCause cause, final Map<DamageModifier, Double> modifiers, final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
        super(damagee, cause, modifiers, modifierFunctions);
        this.damager = damager;
    }

    /**
     * Returns the block that damaged the player.
     *
     * @return Block that damaged the player
     */
    public Block getDamager() {
        return damager;
    }
}
