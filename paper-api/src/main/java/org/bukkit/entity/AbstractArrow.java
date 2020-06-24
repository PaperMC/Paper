package org.bukkit.entity;

import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an arrow.
 */
public interface AbstractArrow extends Projectile {

    /**
     * Gets the knockback strength for an arrow, which is the
     * {@link org.bukkit.enchantments.Enchantment#KNOCKBACK KnockBack} level
     * of the bow that shot it.
     *
     * @return the knockback strength value
     */
    public int getKnockbackStrength();

    /**
     * Sets the knockback strength for an arrow.
     *
     * @param knockbackStrength the knockback strength value
     */
    public void setKnockbackStrength(int knockbackStrength);

    /**
     * Gets the base amount of damage this arrow will do.
     *
     * Defaults to 2.0 for a normal arrow with
     * <code>0.5 * (1 + power level)</code> added for arrows fired from
     * enchanted bows.
     *
     * @return base damage amount
     */
    public double getDamage();

    /**
     * Sets the base amount of damage this arrow will do.
     *
     * @param damage new damage amount
     */
    public void setDamage(double damage);

    /**
     * Gets the number of times this arrow can pierce through an entity.
     *
     * @return pierce level
     */
    public int getPierceLevel();

    /**
     * Sets the number of times this arrow can pierce through an entity.
     *
     * Must be between 0 and 127 times.
     *
     * @param pierceLevel new pierce level
     */
    public void setPierceLevel(int pierceLevel);

    /**
     * Gets whether this arrow is critical.
     * <p>
     * Critical arrows have increased damage and cause particle effects.
     * <p>
     * Critical arrows generally occur when a player fully draws a bow before
     * firing.
     *
     * @return true if it is critical
     */
    public boolean isCritical();

    /**
     * Sets whether or not this arrow should be critical.
     *
     * @param critical whether or not it should be critical
     */
    public void setCritical(boolean critical);

    /**
     * Gets whether this arrow is in a block or not.
     * <p>
     * Arrows in a block are motionless and may be picked up by players.
     *
     * @return true if in a block
     */
    public boolean isInBlock();

    /**
     * Gets the block to which this arrow is attached.
     *
     * @return the attached block or null if not attached
     */
    @Nullable
    public Block getAttachedBlock();

    /**
     * Gets the current pickup status of this arrow.
     *
     * @return the pickup status of this arrow.
     */
    @NotNull
    public PickupStatus getPickupStatus();

    /**
     * Sets the current pickup status of this arrow.
     *
     * @param status new pickup status of this arrow.
     */
    public void setPickupStatus(@NotNull PickupStatus status);

    /**
     * Gets if this arrow was shot from a crossbow.
     *
     * @return if shot from a crossbow
     */
    public boolean isShotFromCrossbow();

    /**
     * Sets if this arrow was shot from a crossbow.
     *
     * @param shotFromCrossbow if shot from a crossbow
     */
    public void setShotFromCrossbow(boolean shotFromCrossbow);

    /**
     * Represents the pickup status of this arrow.
     */
    public enum PickupStatus {
        /**
         * The arrow cannot be picked up.
         */
        DISALLOWED,
        /**
         * The arrow can be picked up.
         */
        ALLOWED,
        /**
         * The arrow can only be picked up by players in creative mode.
         */
        CREATIVE_ONLY
    }
}
