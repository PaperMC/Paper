package org.bukkit.entity;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a falling block
 */
public interface FallingBlock extends Entity {

    /**
     * Get the Material of the falling block
     *
     * @return Material of the block
     * @deprecated use {@link #getBlockData()}
     */
    @Deprecated
    @NotNull
    Material getMaterial();

    /**
     * Get the data for the falling block
     *
     * @return data of the block
     */
    @NotNull
    BlockData getBlockData();

    /**
     * Get if the falling block will break into an item if it cannot be placed
     *
     * @return true if the block will break into an item when obstructed
     */
    boolean getDropItem();

    /**
     * Set if the falling block will break into an item if it cannot be placed
     *
     * @param drop true to break into an item when obstructed
     */
    void setDropItem(boolean drop);

    /**
     * Get the HurtEntities state of this block.
     *
     * @return whether entities will be damaged by this block.
     */
    boolean canHurtEntities();

    /**
     * Set the HurtEntities state of this block.
     *
     * @param hurtEntities whether entities will be damaged by this block.
     */
    void setHurtEntities(boolean hurtEntities);

    /**
     * Get the amount of damage inflicted upon entities multiplied by the distance
     * that the block had fallen when this falling block lands on them.
     *
     * @return the damage per block
     */
    float getDamagePerBlock();

    /**
     * Set the amount of damage inflicted upon entities multiplied by the distance
     * that the block had fallen when this falling block lands on them.
     * <p>
     * If {@code damage} is non-zero, this method will automatically call
     * {@link #setHurtEntities(boolean) setHurtEntities(true)}.
     *
     * @param damage the damage per block to set. Must be >= 0.0
     */
    void setDamagePerBlock(float damage);

    /**
     * Get the maximum amount of damage that can be inflicted upon entities when
     * this falling block lands on them.
     *
     * @return the max damage
     */
    int getMaxDamage();

    /**
     * Set the maximum amount of damage that can be inflicted upon entities when
     * this falling block lands on them.
     * <p>
     * If {@code damage} is non-zero, this method will automatically call
     * {@link #setHurtEntities(boolean) setHurtEntities(true)}.
     *
     * @param damage the max damage to set. Must be >= 0
     */
    void setMaxDamage(int damage);
}
