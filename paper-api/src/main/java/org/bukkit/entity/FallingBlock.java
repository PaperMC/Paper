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
    @Deprecated(since = "1.6.2")
    @NotNull
    Material getMaterial();

    /**
     * Get the data for the falling block
     *
     * @return data of the block
     */
    @NotNull
    BlockData getBlockData();
    // Paper start
    /**
     * Sets the data for the falling block.
     * <br>
     * Any potential additional data currently stored in the falling blocks {@link #getBlockState()} will be
     * purged by calling this setter.
     *
     * @param blockData the data to use as the block
     */
    void setBlockData(@NotNull BlockData blockData);

    /**
     * Get the data of the falling block represented as a {@link org.bukkit.block.BlockState BlockState}
     * which includes potential NBT data that gets applied when the block gets placed on landing.
     *
     * @return the BlockState representing this block
     */
    @NotNull
    org.bukkit.block.BlockState getBlockState();

    /**
     * Sets the {@link BlockData} and possibly present block entity data for the falling block.
     *
     * @param blockState the BlockState to use
     */
    void setBlockState(@NotNull org.bukkit.block.BlockState blockState);
    // Paper end

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

     /**
     * Gets the source block location of the FallingBlock
     *
     * @return the source block location the FallingBlock was spawned from
     * @deprecated replaced by {@link Entity#getOrigin()}
     */
    @Deprecated
    default org.bukkit.Location getSourceLoc() {
        return this.getOrigin();
    }
    // Paper start - Auto expire setting
    /**
     * Sets if this falling block should expire after:
     * - 30 seconds
     * - 5 seconds and is outside of the world
     *
     * @return if this behavior occurs
     */
    boolean doesAutoExpire();

    /**
     * Sets if this falling block should expire after:
     * - 30 seconds
     * - 5 seconds and is outside of the world
     *
     * @param autoExpires if this behavior should occur
     */
    void shouldAutoExpire(boolean autoExpires);
    // Paper end - Auto expire setting
}
