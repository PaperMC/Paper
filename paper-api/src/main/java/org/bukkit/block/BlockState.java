package org.bukkit.block;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.Metadatable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a captured state of a block, which will not change
 * automatically.
 * <p>
 * Unlike Block, which only one object can exist per coordinate, BlockState
 * can exist multiple times for any given Block. Note that another plugin may
 * change the state of the block and you will not know, or they may change the
 * block to another type entirely, causing your BlockState to become invalid.
 */
public interface BlockState extends Metadatable {

    /**
     * Gets the block represented by this block state.
     *
     * @return the block represented by this block state
     * @throws IllegalStateException if this block state is not placed
     */
    @NotNull
    Block getBlock();

    /**
     * Gets the metadata for this block state.
     *
     * @return block specific metadata
     */
    @NotNull
    MaterialData getData();

    /**
     * Gets the data for this block state.
     *
     * @return block specific data
     */
    @NotNull
    BlockData getBlockData();

    /**
     * Gets the type of this block state.
     *
     * @return block type
     */
    @NotNull
    Material getType();

    /**
     * Gets the current light level of the block represented by this block state.
     *
     * @return the light level between 0-15
     * @throws IllegalStateException if this block state is not placed
     */
    byte getLightLevel();

    /**
     * Gets the world which contains the block represented by this block state.
     *
     * @return the world containing the block represented by this block state
     * @throws IllegalStateException if this block state is not placed
     */
    @NotNull
    World getWorld();

    /**
     * Gets the x-coordinate of this block state.
     *
     * @return x-coordinate
     */
    int getX();

    /**
     * Gets the y-coordinate of this block state.
     *
     * @return y-coordinate
     */
    int getY();

    /**
     * Gets the z-coordinate of this block state.
     *
     * @return z-coordinate
     */
    int getZ();

    /**
     * Gets the location of this block state.
     * <p>
     * If this block state is not placed the location's world will be null!
     *
     * @return the location
     */
    @NotNull
    Location getLocation();

    /**
     * Stores the location of this block state in the provided Location object.
     * <p>
     * If the provided Location is null this method does nothing and returns
     * null.
     * <p>
     * If this block state is not placed the location's world will be null!
     *
     * @param loc the location to copy into
     * @return The Location object provided or null
     */
    @Contract("null -> null; !null -> !null")
    @Nullable
    Location getLocation(@Nullable Location loc);

    /**
     * Gets the chunk which contains the block represented by this block state.
     *
     * @return the containing Chunk
     * @throws IllegalStateException if this block state is not placed
     */
    @NotNull
    Chunk getChunk();

    /**
     * Sets the metadata for this block state.
     *
     * @param data New block specific metadata
     */
    void setData(@NotNull MaterialData data);

    /**
     * Sets the data for this block state.
     *
     * @param data New block specific data
     */
    void setBlockData(@NotNull BlockData data);

    /**
     * Sets the type of this block state.
     *
     * @param type Material to change this block state to
     */
    void setType(@NotNull Material type);

    /**
     * Attempts to update the block represented by this state, setting it to
     * the new values as defined by this state.
     * <p>
     * This has the same effect as calling update(false). That is to say,
     * this will not modify the state of a block if it is no longer the same
     * type as it was when this state was taken. It will return false in this
     * eventuality.
     *
     * @return true if the update was successful, otherwise false
     * @see #update(boolean)
     */
    boolean update();

    /**
     * Attempts to update the block represented by this state, setting it to
     * the new values as defined by this state.
     * <p>
     * This has the same effect as calling update(force, true). That is to
     * say, this will trigger a physics update to surrounding blocks.
     *
     * @param force true to forcefully set the state
     * @return true if the update was successful, otherwise false
     */
    boolean update(boolean force);

    /**
     * Attempts to update the block represented by this state, setting it to
     * the new values as defined by this state.
     * <p>
     * If this state is not placed, this will have no effect and return true.
     * <p>
     * Unless force is true, this will not modify the state of a block if it
     * is no longer the same type as it was when this state was taken. It will
     * return false in this eventuality.
     * <p>
     * If force is true, it will set the type of the block to match the new
     * state, set the state data and then return true.
     * <p>
     * If applyPhysics is true, it will trigger a physics update on
     * surrounding blocks which could cause them to update or disappear.
     *
     * @param force true to forcefully set the state
     * @param applyPhysics false to cancel updating physics on surrounding
     *     blocks
     * @return true if the update was successful, otherwise false
     */
    boolean update(boolean force, boolean applyPhysics);

    /**
     * @return The data as a raw byte.
     * @deprecated Magic value
     */
    @Deprecated
    public byte getRawData();

    /**
     * @param data The new data value for the block.
     * @deprecated Magic value
     */
    @Deprecated
    public void setRawData(byte data);

    /**
     * Returns whether this state is placed in the world.
     * <p>
     * Some methods will not work if the block state isn't
     * placed in the world.
     *
     * @return whether the state is placed in the world
     *         or 'virtual' (e.g. on an itemstack)
     */
    boolean isPlaced();
}
