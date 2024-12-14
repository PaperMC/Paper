package org.bukkit.entity;

import java.util.UUID;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a dropped item.
 *
 * @since 1.0.0 R1
 */
public interface Item extends Entity, io.papermc.paper.entity.Frictional { // Paper

    /**
     * Gets the item stack associated with this item drop.
     *
     * @return An item stack.
     */
    @NotNull
    public ItemStack getItemStack();

    /**
     * Sets the item stack associated with this item drop.
     *
     * @param stack An item stack.
     */
    public void setItemStack(@NotNull ItemStack stack);

    /**
     * Gets the delay before this Item is available to be picked up by players
     *
     * @return Remaining delay
     */
    public int getPickupDelay();

    /**
     * Sets the delay before this Item is available to be picked up by players
     *
     * @param delay New delay
     */
    public void setPickupDelay(int delay);

    /**
     * Sets if this Item should live forever
     *
     * @param unlimited true if the lifetime is unlimited
     * @since 1.18.2
     */
    public void setUnlimitedLifetime(boolean unlimited);

    /**
     * Gets if this Item lives forever
     *
     * @return true if the lifetime is unlimited
     * @since 1.18.2
     */
    public boolean isUnlimitedLifetime();

    /**
     * Sets the owner of this item.
     *
     * Other entities will not be able to pickup this item when an owner is set.
     *
     * @param owner UUID of new owner
     * @since 1.13.1
     */
    public void setOwner(@Nullable UUID owner);

    /**
     * Get the owner of this item.
     *
     * @return UUID of owner
     * @since 1.13.1
     */
    @Nullable
    public UUID getOwner();

    /**
     * Set the thrower of this item.
     *
     * The thrower is the entity which dropped the item. This affects the
     * trigger criteria for item pickups, for things such as advancements.
     *
     * @param uuid UUID of thrower
     * @since 1.13.1
     */
    public void setThrower(@Nullable UUID uuid);

    /**
     * Get the thrower of this item.
     *
     * The thrower is the entity which dropped the item.
     *
     * @return UUID of thrower
     * @since 1.13.1
     */
    @Nullable
    public UUID getThrower();

    // Paper start
    /**
     * Gets if non-player entities can pick this Item up
     *
     * @return True if non-player entities can pickup
     * @since 1.12
     */
     public boolean canMobPickup();

    /**
     * Sets if non-player entities can pick this Item up
     *
     * @param canMobPickup True to allow non-player entity pickup
     * @since 1.12
     */
    public void setCanMobPickup(boolean canMobPickup);

    /**
     * Gets whether the player can pickup the item or not
     *
     * @return True if a player can pickup the item
     * @since 1.16.4
     */
    public boolean canPlayerPickup();

    /**
     * Sets whether the item can be picked up or not. Modifies the pickup delay value to do so.
     *
     * @param canPlayerPickup True if the player can pickup the item
     * @since 1.16.4
     */
    public void setCanPlayerPickup(boolean canPlayerPickup);

    /**
     * Gets whether the item will age and despawn from being on the ground too long
     *
     * @return True if the item will age
     * @since 1.16.4
     */
    public boolean willAge();

    /**
     * Sets whether the item will age or not. If the item is not ageing, it will not despawn
     * by being on the ground for too long.
     *
     * @param willAge True if the item should age
     * @since 1.16.4
     */
    public void setWillAge(boolean willAge);

    /**
     * Gets the health of item stack.
     * <p>
     * Currently the default max health is 5.
     *
     * @return the health
     * @since 1.18.1
     */
    public int getHealth();

    /**
     * Sets the health of the item stack. If the value is non-positive
     * the itemstack's normal "on destroy" functionality will be run.
     * <p>
     * Currently, the default max health is 5.
     *
     * @param health the health, a non-positive value will destroy the entity
     * @since 1.18.1
     */
    public void setHealth(int health);
    // Paper end
}
