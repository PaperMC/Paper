package org.bukkit.event.entity;

import com.google.common.base.Function;
import java.util.Map;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity is damaged by an entity
 */
public class EntityDamageByEntityEvent extends EntityDamageEvent {
    private final Entity damager;
    private final boolean critical; // Paper

    @Deprecated(since = "1.20.4", forRemoval = true)
    public EntityDamageByEntityEvent(@NotNull final Entity damager, @NotNull final Entity damagee, @NotNull final DamageCause cause, final double damage) {
        this(damager, damagee, cause, DamageSource.builder(DamageType.GENERIC).withCausingEntity(damager).withDirectEntity(damager).build(), damage);
    }

    @Deprecated
    public EntityDamageByEntityEvent(@NotNull final Entity damager, @NotNull final Entity damagee, @NotNull final DamageCause cause, @NotNull final DamageSource damageSource, final double damage) {
        super(damagee, cause, damageSource, damage);
        this.damager = damager;
        this.critical = false; // Paper - add critical damage API
    }

    @Deprecated(since = "1.20.4", forRemoval = true)
    public EntityDamageByEntityEvent(@NotNull final Entity damager, @NotNull final Entity damagee, @NotNull final DamageCause cause, @NotNull final Map<DamageModifier, Double> modifiers, @NotNull final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
        this(damager, damagee, cause, DamageSource.builder(DamageType.GENERIC).withCausingEntity(damager).withDirectEntity(damager).build(), modifiers, modifierFunctions);
    }

    @Deprecated
    public EntityDamageByEntityEvent(@NotNull final Entity damager, @NotNull final Entity damagee, @NotNull final DamageCause cause, @NotNull final DamageSource damageSource, @NotNull final Map<DamageModifier, Double> modifiers, @NotNull final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
        super(damagee, cause, damageSource, modifiers, modifierFunctions);
        this.damager = damager;
        // Paper start
        this.critical = false;
    }

    @ApiStatus.Internal
    public EntityDamageByEntityEvent(@NotNull final Entity damager, @NotNull final Entity damagee, @NotNull final DamageCause cause, @NotNull final DamageSource damageSource, @NotNull final Map<DamageModifier, Double> modifiers, @NotNull final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions, boolean critical) {
        super(damagee, cause, damageSource, modifiers, modifierFunctions);
        this.damager = damager;
        this.critical = critical;
    }

    /**
     * Shows this damage instance was critical.
     * The damage instance can be critical if the attacking player met the respective conditions.
     * Furthermore, arrows may also cause a critical damage event if the arrow {@link org.bukkit.entity.AbstractArrow#isCritical()}.
     *
     * @return if the hit was critical.
     * @see <a href="https://minecraft.wiki/wiki/Damage#Critical_hit">https://minecraft.wiki/wiki/Damage#Critical_hit</a>
     */
    public boolean isCritical() {
        return this.critical;
    }
    // Paper end

    // Paper start
    /**
     * {@inheritDoc}
     * <p>
     * The {@link DamageSource#getDirectEntity()} may be different from the {@link #getDamager()}
     * if the Minecraft damage source did not originally include an damager entity, but one was included
     * for this event {@link #getDamager()}.
     */
    @Override
    public @NotNull DamageSource getDamageSource() {
        return super.getDamageSource();
    }
    // Paper end

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
