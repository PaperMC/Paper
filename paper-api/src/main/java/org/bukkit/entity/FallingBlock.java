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
     * Get if the falling block will break into an item if it cannot be placed.
     * <p>
     * Note that if {@link #getCancelDrop()} is {@code true}, the falling block
     * will not drop an item regardless of whether or not the returned value is
     * {@code true}.
     *
     * @return true if the block will break into an item when obstructed
     */
    boolean getDropItem();

    /**
     * Set if the falling block will break into an item if it cannot be placed.
     * <p>
     * Note that if {@link #getCancelDrop()} is {@code true}, the falling block
     * will not drop an item regardless of whether or not the value is set to
     * {@code true}.
     *
     * @param drop true to break into an item when obstructed
     */
    void setDropItem(boolean drop);

    /**
     * Get if the falling block will not become a block upon landing and not drop
     * an item.
     * <p>
     * Unlike {@link #getDropItem()}, this property will prevent the block from
     * forming into a block when it lands, causing it to disappear. If this property
     * is true and {@link #getDropItem()} is true, an item will <strong>NOT</strong>
     * be dropped.
     *
     * @return true if the block will disappear
     */
    boolean getCancelDrop();

    /**
     * Get if the falling block will not become a block upon landing and not drop
     * an item.
     * <p>
     * Unlike {@link #setDropItem(boolean)}, this property will prevent the block
     * from forming into a block when it lands, causing it to disappear. If this
     * property is true and {@link #getDropItem()} is true, an item will
     * <strong>NOT</strong> be dropped.
     *
     * @param cancelDrop true to make the block disappear when landing
     */
    void setCancelDrop(boolean cancelDrop);

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
