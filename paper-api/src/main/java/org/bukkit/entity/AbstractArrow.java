package org.bukkit.entity;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

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
     * @see #getWeapon()
     * @deprecated moved to being a function of the firing weapon, always returns 0 here
     */
    @Deprecated(since = "1.21", forRemoval = true)
    public int getKnockbackStrength();

    /**
     * Sets the knockback strength for an arrow.
     *
     * @param knockbackStrength the knockback strength value
     * @see #setWeapon(org.bukkit.inventory.ItemStack)
     * @deprecated moved to being a function of the firing weapon, does nothing here
     */
    @Deprecated(since = "1.21", forRemoval = true)
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
     * @deprecated can be attached to multiple blocks use {@link AbstractArrow#getAttachedBlocks()} instead
     */
    @Nullable
    @Deprecated(since = "1.21.4")
    public Block getAttachedBlock();

    /**
     * Gets the block(s) which this arrow is attached to.
     * All the returned blocks are responsible for preventing
     * the arrow from falling.
     *
     * @return the attached block(s) or an empty list if not attached
     */
    @NotNull
    @Unmodifiable
    List<Block> getAttachedBlocks();

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
     * @see #setWeapon(org.bukkit.inventory.ItemStack)
     * @deprecated a function of the firing weapon instead, this method does nothing
     */
    @Deprecated(since = "1.21", forRemoval = true)
    public void setShotFromCrossbow(boolean shotFromCrossbow);

    /**
     * Gets the ItemStack which will be picked up from this arrow.
     *
     * @return The picked up ItemStack
     * @deprecated use {@link #getItemStack()}
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.20.4") // Paper
    public ItemStack getItem();

    /**
     * Sets the ItemStack which will be picked up from this arrow.
     *
     * @param item ItemStack set to be picked up
     * @deprecated use {@link #setItemStack(ItemStack)}
     */
    @Deprecated(forRemoval = true, since = "1.20.4") // Paper
    public void setItem(@NotNull ItemStack item);

    /**
     * Gets the ItemStack which fired this arrow.
     *
     * @return The firing ItemStack
     */
    @Nullable // Paper
    public ItemStack getWeapon();

    /**
     * Sets the ItemStack which fired this arrow.
     *
     * @param item The firing ItemStack
     */
    public void setWeapon(@NotNull ItemStack item);

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

    // Paper start
    /**
     * Gets the {@link PickupRule} for this arrow.
     *
     * <p>This is generally {@link PickupRule#ALLOWED} only if the arrow was
     * <b>not</b> fired from a bow with the infinity enchantment.</p>
     *
     * @return The pickup rule
     * @deprecated Use {@link Arrow#getPickupStatus()} as an upstream compatible replacement for this function
     */
    @Deprecated
    default PickupRule getPickupRule() {
        return PickupRule.valueOf(this.getPickupStatus().name());
    }

    /**
     * Set the rule for which players can pickup this arrow as an item.
     *
     * @param rule The pickup rule
     * @deprecated Use {@link Arrow#setPickupStatus(PickupStatus)} with {@link PickupStatus} as an upstream compatible replacement for this function
     */
    @Deprecated
    default void setPickupRule(PickupRule rule) {
        this.setPickupStatus(PickupStatus.valueOf(rule.name()));
    }

    @Deprecated
    enum PickupRule {
        DISALLOWED,
        ALLOWED,
        CREATIVE_ONLY;
    }
    // Paper end

    // Paper start - more projectile API
    /**
     * Gets the {@link ItemStack} for this arrow. This stack is used
     * for both visuals on the arrow and the stack that could be picked up.
     *
     * @return The ItemStack, as if a player picked up the arrow
     */
    @NotNull ItemStack getItemStack();

    /**
     * Sets the {@link ItemStack} for this arrow. This stack is used for both
     * visuals on the arrow and the stack that could be picked up.
     *
     * @param stack the arrow stack
     */
    void setItemStack(@NotNull ItemStack stack);

    /**
     * Sets the amount of ticks this arrow has been alive in the world
     * This is used to determine when the arrow should be automatically despawned.
     *
     * @param ticks lifetime ticks
     */
    void setLifetimeTicks(int ticks);

    /**
     * Gets how many ticks this arrow has been in the world for.
     *
     * @return ticks this arrow has been in the world
     */
    int getLifetimeTicks();

    /**
     * Gets the sound that is played when this arrow hits an entity.
     *
     * @return sound that plays
     */
    @NotNull
    org.bukkit.Sound getHitSound();

    /**
     * Sets the sound that is played when this arrow hits an entity.
     *
     * @param sound sound that is played
     */
    void setHitSound(@NotNull org.bukkit.Sound sound);
    // Paper end - more projectile API

    // Paper start - Fix PickupStatus getting reset
    /**
     * Set the shooter of this projectile.
     *
     * @param source the {@link org.bukkit.projectiles.ProjectileSource} that shot this projectile
     * @param resetPickupStatus whether the {@link org.bukkit.entity.AbstractArrow.PickupStatus} should be reset
     */
    void setShooter(@Nullable org.bukkit.projectiles.ProjectileSource source, boolean resetPickupStatus);
    // Paper end - Fix PickupStatus getting reset
}
