package org.bukkit.event.entity;

import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.NumberConversions;

import com.google.common.collect.ImmutableMap;

/**
 * Stores data for damage events
 */
public class EntityDamageEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private static final DamageModifier[] MODIFIERS = DamageModifier.values();
    private final Map<DamageModifier, Double> modifiers;
    private final Map<DamageModifier, Double> originals;
    private boolean cancelled;
    private final DamageCause cause;

    @Deprecated
    public EntityDamageEvent(final Entity damagee, final DamageCause cause, final int damage) {
        this(damagee, cause, (double) damage);
    }

    @Deprecated
    public EntityDamageEvent(final Entity damagee, final DamageCause cause, final double damage) {
        this(damagee, cause, new EnumMap<DamageModifier, Double>(ImmutableMap.of(DamageModifier.BASE, damage)));
    }

    public EntityDamageEvent(final Entity damagee, final DamageCause cause, final Map<DamageModifier, Double> modifiers) {
        super(damagee);
        Validate.isTrue(modifiers.containsKey(DamageModifier.BASE), "BASE DamageModifier missing");
        Validate.isTrue(!modifiers.containsKey(null), "Cannot have null DamageModifier");
        this.originals = new EnumMap<DamageModifier, Double>(modifiers);
        this.cause = cause;
        this.modifiers = modifiers;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Gets the original damage for the specified modifier, as defined at this
     * event's construction.
     *
     * @param type the modifier
     * @throws IllegalArgumentException if type is null
     */
    public double getOriginalDamage(DamageModifier type) throws IllegalArgumentException {
        final Double damage = originals.get(type);
        if (damage != null) {
            return damage;
        }
        if (type == null) {
            throw new IllegalArgumentException("Cannot have null DamageModifier");
        }
        return 0;
    }

    /**
     * Sets the damage for the specified modifier.
     *
     * @param damage the scalar value of the damage's modifier
     * @see #getFinalDamage()
     * @throws IllegalArgumentException if type is null
     * @throws UnsupportedOperationException if the caller does not support
     *     the particular DamageModifier, or to rephrase, when {@link
     *     #isApplicable(DamageModifier)} returns false
     */
    public void setDamage(DamageModifier type, double damage) throws IllegalArgumentException, UnsupportedOperationException {
        if (!modifiers.containsKey(type)) {
            throw type == null ? new IllegalArgumentException("Cannot have null DamageModifier") : new UnsupportedOperationException(type + " is not applicable to " + getEntity());
        }
        modifiers.put(type, damage);
    }

    /**
     * Gets the damage change for some modifier
     *
     * @return The raw amount of damage caused by the event
     * @throws IllegalArgumentException if type is null
     * @see DamageModifier#BASE
     */
    public double getDamage(DamageModifier type) throws IllegalArgumentException {
        Validate.notNull(type, "Cannot have null DamageModifier");
        final Double damage = modifiers.get(type);
        return damage == null ? 0 : damage;
    }

    /**
     * This checks to see if a particular modifier is valid for this event's
     * caller, such that, {@link #setDamage(DamageModifier, double)} will not
     * throw an {@link UnsupportedOperationException}.
     * <p>
     * {@link DamageModifier#BASE} is always applicable.
     *
     * @param type the modifier
     * @return true if the modifier is supported by the caller, false otherwise
     * @throws IllegalArgumentException if type is null
     */
    public boolean isApplicable(DamageModifier type) throws IllegalArgumentException {
        Validate.notNull(type, "Cannot have null DamageModifier");
        return modifiers.containsKey(type);
    }

    /**
     * Gets the raw amount of damage caused by the event
     *
     * @return The raw amount of damage caused by the event
     * @see DamageModifier#BASE
     */
    public double getDamage() {
        return getDamage(DamageModifier.BASE);
    }

    /**
     * Gets the amount of damage caused by the event after all damage
     * reduction is applied.
     *
     * @return the amount of damage caused by the event
     */
    public final double getFinalDamage() {
        double damage = 0;
        for (DamageModifier modifier : MODIFIERS) {
            damage += getDamage(modifier);
        }
        return damage;
    }

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     */
    @Deprecated
    public int _INVALID_getDamage() {
        return NumberConversions.ceil(getDamage());
    }

    /**
     * Sets the raw amount of damage caused by the event
     *
     * @param damage The raw amount of damage caused by the event
     */
    public void setDamage(double damage) {
        setDamage(DamageModifier.BASE, damage);
    }

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     */
    @Deprecated
    public void _INVALID_setDamage(int damage) {
        setDamage(damage);
    }

    /**
     * Gets the cause of the damage.
     *
     * @return A DamageCause value detailing the cause of the damage.
     */
    public DamageCause getCause() {
        return cause;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * An enum to specify the types of modifier
     */
    public enum DamageModifier {
        /**
         * This represents the amount of damage being done, also known as the
         * raw {@link EntityDamageEvent#getDamage()}.
         */
        BASE,
        /**
         * This represents the damage reduced by a wearing a helmet when hit
         * by a falling block.
         */
        HARD_HAT,
        /**
         * This represents  the damage reduction caused by blocking, only present for
         * {@link Player Players}.
         */
        BLOCKING,
        /**
         * This represents the damage reduction caused by wearing armor.
         */
        ARMOR,
        /**
         * This represents the damage reduction caused by the Resistance potion effect.
         */
        RESISTANCE,
        /**
         * This represents the damage reduction caused by the combination of:
         * <ul>
         * <li>
         *     Armor enchantments
         * </li><li>
         *     Witch's potion resistance
         * </li>
         * </ul>
         */
        MAGIC,
        /**
         * This represents the damage reduction caused by the absorption potion
         * effect.
         */
        ABSORPTION,
        ;
    }

    /**
     * An enum to specify the cause of the damage
     */
    public enum DamageCause {

        /**
         * Damage caused when an entity contacts a block such as a Cactus.
         * <p>
         * Damage: 1 (Cactus)
         */
        CONTACT,
        /**
         * Damage caused when an entity attacks another entity.
         * <p>
         * Damage: variable
         */
        ENTITY_ATTACK,
        /**
         * Damage caused when attacked by a projectile.
         * <p>
         * Damage: variable
         */
        PROJECTILE,
        /**
         * Damage caused by being put in a block
         * <p>
         * Damage: 1
         */
        SUFFOCATION,
        /**
         * Damage caused when an entity falls a distance greater than 3 blocks
         * <p>
         * Damage: fall height - 3.0
         */
        FALL,
        /**
         * Damage caused by direct exposure to fire
         * <p>
         * Damage: 1
         */
        FIRE,
        /**
         * Damage caused due to burns caused by fire
         * <p>
         * Damage: 1
         */
        FIRE_TICK,
        /**
         * Damage caused due to a snowman melting
         * <p>
         * Damage: 1
         */
        MELTING,
        /**
         * Damage caused by direct exposure to lava
         * <p>
         * Damage: 4
         */
        LAVA,
        /**
         * Damage caused by running out of air while in water
         * <p>
         * Damage: 2
         */
        DROWNING,
        /**
         * Damage caused by being in the area when a block explodes.
         * <p>
         * Damage: variable
         */
        BLOCK_EXPLOSION,
        /**
         * Damage caused by being in the area when an entity, such as a
         * Creeper, explodes.
         * <p>
         * Damage: variable
         */
        ENTITY_EXPLOSION,
        /**
         * Damage caused by falling into the void
         * <p>
         * Damage: 4 for players
         */
        VOID,
        /**
         * Damage caused by being struck by lightning
         * <p>
         * Damage: 5
         */
        LIGHTNING,
        /**
         * Damage caused by committing suicide using the command "/kill"
         * <p>
         * Damage: 1000
         */
        SUICIDE,
        /**
         * Damage caused by starving due to having an empty hunger bar
         * <p>
         * Damage: 1
         */
        STARVATION,
        /**
         * Damage caused due to an ongoing poison effect
         * <p>
         * Damage: 1
         */
        POISON,
        /**
         * Damage caused by being hit by a damage potion or spell
         * <p>
         * Damage: variable
         */
        MAGIC,
        /**
         * Damage caused by Wither potion effect
         */
        WITHER,
        /**
         * Damage caused by being hit by a falling block which deals damage
         * <p>
         * <b>Note:</b> Not every block deals damage
         * <p>
         * Damage: variable
         */
        FALLING_BLOCK,
        /**
         * Damage caused in retaliation to another attack by the Thorns
         * enchantment.
         * <p>
         * Damage: 1-4 (Thorns)
         */
        THORNS,
        /**
         * Custom damage.
         * <p>
         * Damage: variable
         */
        CUSTOM
    }
}
