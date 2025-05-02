package org.bukkit.block;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.Metadatable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import java.util.Collection;

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
     * @deprecated use {@link #getBlockData()}
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.13")
    MaterialData getData();

    /**
     * Gets the data for this block state.
     *
     * @return block specific data
     */
    @NotNull
    BlockData getBlockData();

    /**
     * Returns a copy of this BlockState as an unplaced BlockState.
     *
     * @return a copy of the block state
     */
    @NotNull
    @ApiStatus.Experimental
    BlockState copy();

    /**
     * Copies the state to another block as an unplaced BlockState.
     *
     * @param location the location to copy the block state to
     * @return the new block state
     */
    @NotNull
    @ApiStatus.Experimental
    BlockState copy(@NotNull Location location);

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
     * @deprecated use {@link #setBlockData(BlockData)}
     */
    @Deprecated(forRemoval = true, since = "1.13")
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
    @Deprecated(since = "1.6.2", forRemoval = true)
    byte getRawData();

    /**
     * @param data The new data value for the block.
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2", forRemoval = true)
    void setRawData(byte data);

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

    /**
     * Checks if this block state is collidable.
     *
     * @return true if collidable
     */
    boolean isCollidable();

    /**
     * Returns an immutable list of items which would drop by destroying this block state.
     *
     * @return an immutable list of dropped items for the block state
     * @throws IllegalStateException if this block state is not placed
     */
    @NotNull
    default @Unmodifiable Collection<ItemStack> getDrops() {
        return this.getDrops(null);
    }

    /**
     * Returns an immutable list of items which would drop by destroying this block state
     * with a specific tool
     *
     * @param tool The tool or item in hand used for digging
     * @return an immutable list of dropped items for the block state
     * @throws IllegalStateException if this block state is not placed
     */
    @NotNull
    default @Unmodifiable Collection<ItemStack> getDrops(@Nullable ItemStack tool) {
        return this.getDrops(tool, null);
    }

    /**
     * Returns an immutable list of items which would drop by the entity destroying this
     * block state with a specific tool
     *
     * @param tool The tool or item in hand used for digging
     * @param entity the entity destroying the block
     * @return an immutable list of dropped items for the block state
     * @throws IllegalStateException if this block state is not placed
     */
    @NotNull
    @Unmodifiable
    Collection<ItemStack> getDrops(@Nullable ItemStack tool, @Nullable Entity entity);

    /**
     * Checks if the block state can suffocate.
     *
     * @return {@code true} if the block state can suffocate
     * @throws IllegalStateException if this block state is not placed
     */
    boolean isSuffocating();
}
