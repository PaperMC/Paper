package org.bukkit.entity;

import org.bukkit.MinecraftExperimental;
import org.bukkit.MinecraftExperimental.Requires;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an ominous item spawner.
 */
@MinecraftExperimental(Requires.UPDATE_1_21)
@ApiStatus.Experimental
public interface OminousItemSpawner extends Entity {

    /**
     * Gets the item which will be spawned by this spawner.
     *
     * @return the item
     */
    @Nullable
    ItemStack getItem();

    /**
     * Sets the item which will be spawned by this spawner.
     *
     * @param item the item
     */
    void setItem(@Nullable ItemStack item);

    /**
     * Gets the ticks after which this item will be spawned.
     *
     * @return total spawn ticks
     */
    long getSpawnItemAfterTicks();

    /**
     * Sets the ticks after which this item will be spawned.
     *
     * @param ticks total spawn ticks
     */
    void setSpawnItemAfterTicks(long ticks);
}
