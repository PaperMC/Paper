package org.bukkit.craftbukkit.event.entity;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class CraftEntityDamageEvent extends CraftEntityEvent implements EntityDamageEvent {

    private static final DamageModifier[] MODIFIERS = DamageModifier.values();
    private static final Function<? super Double, Double> ZERO = Functions.constant(-0.0);
    private final Map<DamageModifier, Double> modifiers;
    private final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions;
    private final Map<DamageModifier, Double> originals;
    private final DamageCause cause;
    private final DamageSource damageSource;

    private boolean cancelled;

    public CraftEntityDamageEvent(final Entity damagee, final DamageCause cause, final DamageSource damageSource, final Map<DamageModifier, Double> modifiers, final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
        super(damagee);
        Preconditions.checkArgument(modifiers.containsKey(DamageModifier.BASE), "BASE DamageModifier missing");
        Preconditions.checkArgument(!modifiers.containsKey(null), "Cannot have null DamageModifier");
        Preconditions.checkArgument(modifiers.values().stream().allMatch(Objects::nonNull), "Cannot have null modifier values");
        Preconditions.checkArgument(modifiers.keySet().equals(modifierFunctions.keySet()), "Must have a modifier function for each DamageModifier");
        Preconditions.checkArgument(modifierFunctions.values().stream().allMatch(Objects::nonNull), "Cannot have null modifier function");
        this.originals = new EnumMap<>(modifiers);
        this.cause = cause;
        this.modifiers = modifiers;
        this.modifierFunctions = modifierFunctions;
        this.damageSource = damageSource;
    }

    @Override
    public double getOriginalDamage(final DamageModifier modifier) throws IllegalArgumentException {
        Preconditions.checkArgument(modifier != null, "Cannot have null DamageModifier");
        final Double damage = this.originals.get(modifier);
        return (damage != null) ? damage : 0;
    }

    @Override
    public void setDamage(final DamageModifier modifier, final double damage) throws IllegalArgumentException, UnsupportedOperationException {
        Preconditions.checkArgument(modifier != null, "Cannot have null DamageModifier");
        if (!this.modifiers.containsKey(modifier)) {
            throw new UnsupportedOperationException(modifier + " is not applicable to " + getEntity());
        }
        this.modifiers.put(modifier, damage);
    }

    @Override
    public double getDamage(final DamageModifier modifier) throws IllegalArgumentException {
        Preconditions.checkArgument(modifier != null, "Cannot have null DamageModifier");
        final Double damage = this.modifiers.get(modifier);
        return damage == null ? 0 : damage;
    }

    @Override
    public boolean isApplicable(final DamageModifier modifier) throws IllegalArgumentException {
        Preconditions.checkArgument(modifier != null, "Cannot have null DamageModifier");
        return this.modifiers.containsKey(modifier);
    }

    @Override
    public double getFinalDamage() {
        double damage = 0;
        for (final DamageModifier modifier : MODIFIERS) {
            damage += this.getDamage(modifier);
        }
        return damage;
    }

    @Override
    public void setDamage(final double damage) {
        // These have to happen in the same order as the server calculates them, keep the enum sorted
        double remaining = damage;
        double oldRemaining = this.getDamage(DamageModifier.BASE);
        for (final DamageModifier modifier : MODIFIERS) {
            if (!this.isApplicable(modifier)) {
                continue;
            }

            final Function<? super Double, Double> modifierFunction = modifierFunctions.get(modifier);
            final double newVanilla = modifierFunction.apply(remaining);
            final double oldVanilla = modifierFunction.apply(oldRemaining);
            final double difference = oldVanilla - newVanilla;

            // Don't allow value to cross zero, assume zero values should be negative
            final double old = this.getDamage(modifier);
            if (old > 0) {
                this.setDamage(modifier, Math.max(0, old - difference));
            } else {
                this.setDamage(modifier, Math.min(0, old - difference));
            }
            remaining += newVanilla;
            oldRemaining += oldVanilla;
        }

        this.setDamage(DamageModifier.BASE, damage);
    }

    @Override
    public DamageCause getCause() {
        return this.cause;
    }

    @Override
    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityDamageEvent.getHandlerList();
    }
}
