package org.bukkit.event.entity;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/// Stores data for damage events
public class EntityDamageEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private static final DamageModifier[] MODIFIERS = DamageModifier.values();
    private static final Function<? super Double, Double> ZERO = Functions.constant(-0.0);
    private final Map<DamageModifier, Double> modifiers;
    private final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions;
    private final Map<DamageModifier, Double> originals;
    private final DamageCause cause;
    private final DamageSource damageSource;

    private boolean cancelled;

    @ApiStatus.Internal
    @Deprecated(since = "1.20.4", forRemoval = true)
    public EntityDamageEvent(@NotNull final Entity damagee, @NotNull final DamageCause cause, final double damage) {
        this(damagee, cause, DamageSource.builder(DamageType.GENERIC).build(), damage);
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public EntityDamageEvent(@NotNull final Entity damagee, @NotNull final DamageCause cause, @NotNull final DamageSource damageSource, final double damage) {
        this(damagee, cause, damageSource, new EnumMap<>(ImmutableMap.of(DamageModifier.BASE, damage)), new EnumMap<DamageModifier, Function<? super Double, Double>>(ImmutableMap.of(DamageModifier.BASE, ZERO)));
    }

    @ApiStatus.Internal
    @Deprecated(since = "1.20.4", forRemoval = true)
    public EntityDamageEvent(@NotNull final Entity damagee, @NotNull final DamageCause cause, @NotNull final Map<DamageModifier, Double> modifiers, @NotNull final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
        this(damagee, cause, DamageSource.builder(DamageType.GENERIC).build(), modifiers, modifierFunctions);
    }

    @ApiStatus.Internal
    public EntityDamageEvent(@NotNull final Entity damagee, @NotNull final DamageCause cause, @NotNull final DamageSource damageSource, @NotNull final Map<DamageModifier, Double> modifiers, @NotNull final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
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

    /// Gets the original damage for the specified modifier, as defined at this
    /// event's construction.
    ///
    /// @param type the modifier
    /// @return the original damage
    public double getOriginalDamage(@NotNull DamageModifier type) throws IllegalArgumentException {
        Preconditions.checkArgument(type != null, "Cannot have null DamageModifier");
        final Double damage = this.originals.get(type);
        return (damage != null) ? damage : 0;
    }

    /// Sets the damage for the specified modifier.
    ///
    /// @param type the damage modifier
    /// @param damage the scalar value of the damage's modifier
    /// @throws UnsupportedOperationException if the caller does not support
    ///     the particular DamageModifier, or to rephrase, when
    /// [#isApplicable(DamageModifier)] returns false
    /// @see #getFinalDamage()
    public void setDamage(@NotNull DamageModifier type, double damage) throws IllegalArgumentException, UnsupportedOperationException {
        Preconditions.checkArgument(type != null, "Cannot have null DamageModifier");
        if (!this.modifiers.containsKey(type)) {
            throw new UnsupportedOperationException(type + " is not applicable to " + getEntity());
        }
        this.modifiers.put(type, damage);
    }

    /// Gets the damage change for some modifier
    ///
    /// @param type the damage modifier
    /// @return The raw amount of damage caused by the event
    /// @see DamageModifier#BASE
    public double getDamage(@NotNull DamageModifier type) throws IllegalArgumentException {
        Preconditions.checkArgument(type != null, "Cannot have null DamageModifier");
        final Double damage = this.modifiers.get(type);
        return damage == null ? 0 : damage;
    }

    /// This checks to see if a particular modifier is valid for this event's
    /// caller, such that, [#setDamage(DamageModifier, double)] will not
    /// throw an [UnsupportedOperationException].
    ///
    /// [DamageModifier#BASE] is always applicable.
    ///
    /// @param type the modifier
    /// @return `true` if the modifier is supported by the caller, `false` otherwise
    public boolean isApplicable(@NotNull DamageModifier type) throws IllegalArgumentException {
        Preconditions.checkArgument(type != null, "Cannot have null DamageModifier");
        return this.modifiers.containsKey(type);
    }

    /// Gets the raw amount of damage caused by the event
    ///
    /// @return The raw amount of damage caused by the event
    /// @see DamageModifier#BASE
    public double getDamage() {
        return this.getDamage(DamageModifier.BASE);
    }

    /// Gets the amount of damage caused by the event after all damage
    /// reduction is applied.
    ///
    /// @return the amount of damage caused by the event
    public final double getFinalDamage() {
        double damage = 0;
        for (DamageModifier modifier : MODIFIERS) {
            damage += this.getDamage(modifier);
        }
        return damage;
    }

    /// Sets the raw amount of damage caused by the event.
    ///
    /// For compatibility this also recalculates the modifiers and scales
    /// them by the difference between the modifier for the previous damage
    /// value and the new one.
    ///
    /// @param damage The raw amount of damage caused by the event
    public void setDamage(double damage) {
        // These have to happen in the same order as the server calculates them, keep the enum sorted
        double remaining = damage;
        double oldRemaining = this.getDamage(DamageModifier.BASE);
        for (DamageModifier modifier : MODIFIERS) {
            if (!this.isApplicable(modifier)) {
                continue;
            }

            Function<? super Double, Double> modifierFunction = modifierFunctions.get(modifier);
            double newVanilla = modifierFunction.apply(remaining);
            double oldVanilla = modifierFunction.apply(oldRemaining);
            double difference = oldVanilla - newVanilla;

            // Don't allow value to cross zero, assume zero values should be negative
            double old = this.getDamage(modifier);
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

    /// Gets the cause of the damage.
    ///
    /// While a DamageCause may indicate a specific Bukkit-assigned cause of damage,
    /// [#getDamageSource()] may expose additional types of damage such as custom
    /// damage types provided by data packs, as well as any direct or indirect entities,
    /// locations, or other contributing factors to the damage being inflicted. The
    /// alternative is generally preferred, but DamageCauses provided to this event
    /// should largely encompass most common use cases for developers if a simple cause
    /// is required.
    ///
    /// @return a DamageCause value detailing the cause of the damage.
    @NotNull
    public DamageCause getCause() {
        return this.cause;
    }

    /// Get the source of damage.
    ///
    /// @return a DamageSource detailing the source of the damage.
    @NotNull
    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /// An enum to specify the types of modifier
    ///
    /// @deprecated This API is responsible for a large number of implementation
    /// problems and is in general unsustainable to maintain.
    @Deprecated(since = "1.12")
    public enum DamageModifier {
        /// This represents the amount of damage being done, also known as the
        /// raw [EntityDamageEvent#getDamage()].
        BASE,
        INVULNERABILITY_REDUCTION, // Paper - fix invulnerability reduction in EntityDamageEvent - needs to be right under BASE as it's the first reduction all others are based on
        /// This represents the damage increased by freezing status.
        FREEZING,
        /// This represents the damage reduced by a wearing a helmet when hit
        /// by a falling block.
        HARD_HAT,
        /// This represents  the damage reduction caused by blocking, only present for
        /// [`Players`][Player].
        BLOCKING,
        /// This represents the damage reduction caused by wearing armor.
        ARMOR,
        /// This represents the damage reduction caused by the Resistance potion effect.
        RESISTANCE,
        /// This represents the damage reduction caused by the combination of:
        ///
        ///   - Armor enchantments
        ///   - Witch's potion resistance
        ///
        MAGIC,
        /// This represents the damage reduction caused by the absorption potion
        /// effect.
        ABSORPTION,
        ;
    }

    /// An enum to specify the cause of the damage
    public enum DamageCause {

        /// Damage caused by /kill command.
        ///
        /// Damage: [Float#MAX_VALUE]
        KILL,
        /// Damage caused by the World Border.
        ///
        /// Damage: [WorldBorder#getDamageAmount()]
        WORLD_BORDER,
        /// Damage caused when an entity contacts another entity (sulfur cube) or block (cactus, dripstone stalagmite,
        /// berry bush, campfire, magma block).
        ///
        /// Damage: variable
        CONTACT,
        /// Damage caused when an entity attacks another entity.
        ///
        /// Damage: variable
        ENTITY_ATTACK,
        /// Damage caused when an entity attacks another entity in a sweep attack.
        ///
        /// Damage: variable
        ENTITY_SWEEP_ATTACK,
        /// Damage caused when attacked by a projectile.
        ///
        /// Damage: variable
        PROJECTILE,
        /// Damage caused by being put in a block.
        ///
        /// Damage: 1
        SUFFOCATION,
        /// Damage caused when an entity falls a distance greater than the [`safe fall distance`][org.bukkit.attribute.Attribute#SAFE_FALL_DISTANCE].
        ///
        /// Damage: fall height - [`safe fall distance`][org.bukkit.attribute.Attribute#SAFE_FALL_DISTANCE]
        FALL,
        /// Damage caused by direct exposure to fire.
        ///
        /// Damage: 1 or 2 (for soul fire)
        FIRE,
        /// Damage caused due to burns caused by fire.
        ///
        /// Damage: 1
        FIRE_TICK,
        /// Damage caused due to a snowman melting.
        ///
        /// Damage: 1
        MELTING,
        /// Damage caused by direct exposure to lava.
        ///
        /// Damage: 4
        LAVA,
        /// Damage caused by running out of air while in water.
        ///
        /// Damage: 1 or 2
        DROWNING,
        /// Damage caused by being in the area when a block explodes.
        ///
        /// Damage: variable
        BLOCK_EXPLOSION,
        /// Damage caused by being in the area when an entity, such as a
        /// Creeper, explodes.
        ///
        /// Damage: variable
        ENTITY_EXPLOSION,
        /// Damage caused by falling into the void.
        ///
        /// Damage: [org.bukkit.World#getVoidDamageAmount()]
        VOID,
        /// Damage caused by being struck by lightning.
        ///
        /// Damage: 5 or [Float#MAX_VALUE] for turtle
        LIGHTNING,
        /// Damage caused by committing suicide.
        ///
        /// **Note:** This is currently only used by plugins, default commands
        /// like /minecraft:kill use [#KILL] to damage players.
        ///
        /// Damage: variable
        SUICIDE,
        /// Damage caused by starving due to having an empty hunger bar.
        ///
        /// Damage: 1
        STARVATION,
        /// Damage caused due to an ongoing poison effect.
        ///
        /// Damage: 1
        POISON,
        /// Damage caused by being hit by a damage potion or spell.
        ///
        /// Damage: variable
        MAGIC,
        /// Damage caused by Wither potion effect
        WITHER,
        /// Damage caused by being hit by a falling block which deals damage.
        ///
        /// **Note:** Not every block deals damage
        ///
        /// Damage: variable
        FALLING_BLOCK,
        /// Damage caused in retaliation to another attack by the [org.bukkit.enchantments.Enchantment#THORNS]
        /// enchantment or guardian.
        ///
        /// Damage: 1-5 (thorns) or 2 (guardian)
        THORNS,
        /// Damage caused by a dragon breathing fire.
        ///
        /// Damage: variable
        ///
        /// @deprecated never used without help of commands or plugins,
        /// [#ENTITY_ATTACK] will be used instead
        @Deprecated(since = "1.21.5")
        DRAGON_BREATH,
        /// Damage caused when an entity runs into a wall.
        ///
        /// Damage: variable
        FLY_INTO_WALL,
        /// Damage caused when an entity steps on [Material#MAGMA_BLOCK].
        ///
        /// Damage: 1
        ///
        /// @deprecated use [#CONTACT], the block will be exposed in the event
        @Deprecated(since = "26.2")
        HOT_FLOOR,
        /// Damage caused when an entity steps on [Material#CAMPFIRE] or [Material#SOUL_CAMPFIRE].
        ///
        /// Damage: 1 or 2 (for soul fire)
        ///
        /// @deprecated use [#CONTACT], the block will be exposed in the event
        @Deprecated(since = "26.2")
        CAMPFIRE,
        /// Damage caused when an entity is colliding with too many entities due
        /// to the [org.bukkit.GameRules#MAX_ENTITY_CRAMMING].
        ///
        /// Damage: 6
        CRAMMING,
        /// Damage caused when an entity that should be in water is not.
        ///
        /// Damage: 1 or 2
        DRYOUT,
        /// Damage caused from freezing.
        ///
        /// Damage: 1 or 5 (for [`sensitive`][org.bukkit.Tag#ENTITY_TYPES_FREEZE_HURTS_EXTRA_TYPES] entities)
        FREEZE,
        /// Damage caused by the Sonic Boom attack from [org.bukkit.entity.Warden].
        ///
        /// Damage: 10
        SONIC_BOOM,
        /// Custom damage.
        ///
        /// Damage: variable
        CUSTOM;
    }
}
