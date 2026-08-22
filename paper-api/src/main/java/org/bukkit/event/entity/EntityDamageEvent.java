package org.bukkit.event.entity;

import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Stores data for damage events.
 */
public interface EntityDamageEvent extends EntityEvent, Cancellable {

    /**
     * Gets the original damage for the specified modifier, as defined at this
     * event's construction.
     *
     * @param modifier the modifier
     * @return the original damage
     */
    double getOriginalDamage(DamageModifier modifier) throws IllegalArgumentException;

    /**
     * Sets the damage for the specified modifier.
     *
     * @param modifier the damage modifier
     * @param damage the scalar value of the damage's modifier
     * @throws UnsupportedOperationException if the caller does not support
     *     the particular DamageModifier, or to rephrase, when {@link
     *     #isApplicable(DamageModifier)} returns false
     * @see #getFinalDamage()
     */
    void setDamage(DamageModifier modifier, double damage) throws UnsupportedOperationException;

    /**
     * Gets the damage change for some modifier
     *
     * @param modifier the damage modifier
     * @return The raw amount of damage caused by the event
     * @see DamageModifier#BASE
     */
    double getDamage(DamageModifier modifier);

    /**
     * This checks to see if a particular modifier is valid for this event's
     * caller, such that, {@link #setDamage(DamageModifier, double)} will not
     * throw an {@link UnsupportedOperationException}.
     * <p>
     * {@link DamageModifier#BASE} is always applicable.
     *
     * @param modifier the modifier
     * @return {@code true} if the modifier is supported by the caller, {@code false} otherwise
     */
    boolean isApplicable(DamageModifier modifier);

    /**
     * Gets the raw amount of damage caused by the event
     *
     * @return The raw amount of damage caused by the event
     * @see DamageModifier#BASE
     */
    default double getDamage() {
        return this.getDamage(DamageModifier.BASE);
    }

    /**
     * Gets the amount of damage caused by the event after all damage
     * reduction is applied.
     *
     * @return the amount of damage caused by the event
     */
    double getFinalDamage();

    /**
     * Sets the raw amount of damage caused by the event.
     * <p>
     * For compatibility this also recalculates the modifiers and scales
     * them by the difference between the modifier for the previous damage
     * value and the new one.
     *
     * @param damage The raw amount of damage caused by the event
     */
    void setDamage(double damage);

    /**
     * Gets the cause of the damage.
     * <p>
     * While a DamageCause may indicate a specific Bukkit-assigned cause of damage,
     * {@link #getDamageSource()} may expose additional types of damage such as custom
     * damage types provided by data packs, as well as any direct or indirect entities,
     * locations, or other contributing factors to the damage being inflicted. The
     * alternative is generally preferred, but DamageCauses provided to this event
     * should largely encompass most common use cases for developers if a simple cause
     * is required.
     *
     * @return a DamageCause value detailing the cause of the damage.
     */
    DamageCause getCause();

    /**
     * Get the source of damage.
     *
     * @return a DamageSource detailing the source of the damage.
     */
    DamageSource getDamageSource();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * An enum to specify the types of modifier
     *
     * @deprecated This API is responsible for a large number of implementation
     * problems and is in general unsustainable to maintain.
     */
    @Deprecated(since = "1.12")
    enum DamageModifier {
        /**
         * This represents the amount of damage being done, also known as the
         * raw {@link EntityDamageEvent#getDamage()}.
         */
        BASE,
        INVULNERABILITY_REDUCTION, // Paper - fix invulnerability reduction in EntityDamageEvent - needs to be right under BASE as it's the first reduction all others are based on
        /**
         * This represents the damage increased by freezing status.
         */
        FREEZING,
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
    }

    /**
     * An enum to specify the cause of the damage
     */
    enum DamageCause {

        /**
         * Damage caused by /kill command.
         * <p>
         * Damage: {@link Float#MAX_VALUE}
         */
        KILL,
        /**
         * Damage caused by the World Border.
         * <p>
         * Damage: {@link WorldBorder#getDamageAmount()} <!-- todo not accurate -->
         */
        WORLD_BORDER,
        /**
         * Damage caused when an entity contacts another entity (sulfur cube) or block (cactus, dripstone stalagmite,
         * berry bush, campfire, magma block).
         * <p>
         * Damage: variable
         */
        CONTACT,
        /**
         * Damage caused when an entity attacks another entity.
         * <p>
         * Damage: variable
         */
        ENTITY_ATTACK,
        /**
         * Damage caused when an entity attacks another entity in a sweep attack.
         * <p>
         * Damage: variable
         */
        ENTITY_SWEEP_ATTACK,
        /**
         * Damage caused when attacked by a projectile.
         * <p>
         * Damage: variable
         */
        PROJECTILE,
        /**
         * Damage caused by being put in a block.
         * <p>
         * Damage: 1
         */
        SUFFOCATION,
        /**
         * Damage caused when an entity falls a distance greater than the {@link org.bukkit.attribute.Attribute#SAFE_FALL_DISTANCE safe fall distance}.
         * <p>
         * Damage: fall height - {@link org.bukkit.attribute.Attribute#SAFE_FALL_DISTANCE safe fall distance} <!-- todo not accurate -->
         */
        FALL,
        /**
         * Damage caused by direct exposure to fire.
         * <p>
         * Damage: 1 or 2 (for soul fire)
         */
        FIRE,
        /**
         * Damage caused due to burns caused by fire.
         * <p>
         * Damage: 1
         */
        FIRE_TICK,
        /**
         * Damage caused due to a snowman melting.
         * <p>
         * Damage: 1
         */
        MELTING,
        /**
         * Damage caused by direct exposure to lava.
         * <p>
         * Damage: 4
         */
        LAVA,
        /**
         * Damage caused by running out of air while in water.
         * <p>
         * Damage: 1 or 2
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
         * Damage caused by falling into the void.
         * <p>
         * Damage: {@link org.bukkit.World#getVoidDamageAmount()}
         */
        VOID,
        /**
         * Damage caused by being struck by lightning.
         * <p>
         * Damage: 5 or {@link Float#MAX_VALUE} for turtle
         */
        LIGHTNING,
        /**
         * Damage caused by committing suicide.
         * <p>
         * <b>Note:</b> This is currently only used by plugins, default commands
         * like /minecraft:kill use {@link #KILL} to damage players.
         * <p>
         * Damage: variable
         */
        SUICIDE,
        /**
         * Damage caused by starving due to having an empty hunger bar.
         * <p>
         * Damage: 1
         */
        STARVATION,
        /**
         * Damage caused due to an ongoing poison effect.
         * <p>
         * Damage: 1
         */
        POISON,
        /**
         * Damage caused by being hit by a damage potion or spell.
         * <p>
         * Damage: variable
         */
        MAGIC,
        /**
         * Damage caused by Wither potion effect
         */
        WITHER,
        /**
         * Damage caused by being hit by a falling block which deals damage.
         * <p>
         * <b>Note:</b> Not every block deals damage
         * <p>
         * Damage: variable
         */
        FALLING_BLOCK,
        /**
         * Damage caused in retaliation to another attack by the {@link org.bukkit.enchantments.Enchantment#THORNS}
         * enchantment or guardian.
         * <p>
         * Damage: 1-5 (thorns) or 2 (guardian)
         */
        THORNS,
        /**
         * Damage caused by a dragon breathing fire.
         * <p>
         * Damage: variable
         *
         * @deprecated never used without help of commands or plugins,
         * {@link #ENTITY_ATTACK} will be used instead
         */
        @Deprecated(since = "1.21.5")
        DRAGON_BREATH,
        /**
         * Damage caused when an entity runs into a wall.
         * <p>
         * Damage: variable
         */
        FLY_INTO_WALL,
        /**
         * Damage caused when an entity steps on {@link Material#MAGMA_BLOCK}.
         * <p>
         * Damage: 1
         *
         * @deprecated use {@link #CONTACT}, the block will be exposed in the event
         */
        @Deprecated(since = "26.2")
        HOT_FLOOR,
        /**
         * Damage caused when an entity steps on {@link Material#CAMPFIRE} or {@link Material#SOUL_CAMPFIRE}.
         * <p>
         * Damage: 1 or 2 (for soul fire)
         *
         * @deprecated use {@link #CONTACT}, the block will be exposed in the event
         */
        @Deprecated(since = "26.2")
        CAMPFIRE,
        /**
         * Damage caused when an entity is colliding with too many entities due
         * to the {@link org.bukkit.GameRules#MAX_ENTITY_CRAMMING}.
         * <p>
         * Damage: 6
         */
        CRAMMING,
        /**
         * Damage caused when an entity that should be in water is not.
         * <p>
         * Damage: 1 or 2
         */
        DRYOUT,
        /**
         * Damage caused from freezing.
         * <p>
         * Damage: 1 or 5 (for {@link org.bukkit.Tag#ENTITY_TYPES_FREEZE_HURTS_EXTRA_TYPES sensitive} entities)
         */
        FREEZE,
        /**
         * Damage caused by the Sonic Boom attack from {@link org.bukkit.entity.Warden}.
         * <p>
         * Damage: 10
         */
        SONIC_BOOM,
        /**
         * Custom damage.
         * <p>
         * Damage: variable
         */
        CUSTOM
    }
}
