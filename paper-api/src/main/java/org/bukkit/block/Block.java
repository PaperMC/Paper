package org.bukkit.block;

import java.util.Collection;
import org.bukkit.Chunk;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Translatable;
import org.bukkit.World;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.Metadatable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.util.VoxelShape;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a block. This is a live object, and only one Block may exist for
 * any given location in a world. The state of the block may change
 * concurrently to your own handling of it; use block.getState() to get a
 * snapshot state of a block which will not be modified.
 *
 * <br>
 * Note that parts of this class which require access to the world at large
 * (i.e. lighting and power) may not be able to be safely accessed during world
 * generation when used in cases like BlockPhysicsEvent!!!!
 */
public interface Block extends Metadatable, Translatable, net.kyori.adventure.translation.Translatable { // Paper - translatable

    /**
     * Gets the metadata for this block
     *
     * @return block specific metadata
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2", forRemoval = true)
    byte getData();

    /**
     * Gets the complete block data for this block
     *
     * @return block specific data
     */
    @NotNull
    BlockData getBlockData();

    /**
     * Gets the block at the given offsets
     *
     * @param modX X-coordinate offset
     * @param modY Y-coordinate offset
     * @param modZ Z-coordinate offset
     * @return Block at the given offsets
     */
    @NotNull
    Block getRelative(int modX, int modY, int modZ);

    /**
     * Gets the block at the given face
     * <p>
     * This method is equal to getRelative(face, 1)
     *
     * @param face Face of this block to return
     * @return Block at the given face
     * @see #getRelative(BlockFace, int)
     */
    @NotNull
    Block getRelative(@NotNull BlockFace face);

    /**
     * Gets the block at the given distance of the given face
     * <p>
     * For example, the following method places water at 100,102,100; two
     * blocks above 100,100,100.
     *
     * <pre>
     * Block block = world.getBlockAt(100, 100, 100);
     * Block shower = block.getRelative(BlockFace.UP, 2);
     * shower.setType(Material.WATER);
     * </pre>
     *
     * @param face Face of this block to return
     * @param distance Distance to get the block at
     * @return Block at the given face
     */
    @NotNull
    Block getRelative(@NotNull BlockFace face, int distance);

    /**
     * Gets the type of this block
     *
     * @return block type
     */
    @NotNull
    Material getType();

    /**
     * Gets the light level between 0-15
     *
     * @return light level
     */
    byte getLightLevel();

    /**
     * Get the amount of light at this block from the sky.
     * <p>
     * Any light given from other sources (such as blocks like torches) will
     * be ignored.
     *
     * @return Sky light level
     */
    byte getLightFromSky();

    /**
     * Get the amount of light at this block from nearby blocks.
     * <p>
     * Any light given from other sources (such as the sun) will be ignored.
     *
     * @return Block light level
     */
    byte getLightFromBlocks();

    /**
     * Gets the world which contains this Block
     *
     * @return World containing this block
     */
    @NotNull
    World getWorld();

    /**
     * Gets the x-coordinate of this block
     *
     * @return x-coordinate
     */
    int getX();

    /**
     * Gets the y-coordinate of this block
     *
     * @return y-coordinate
     */
    int getY();

    /**
     * Gets the z-coordinate of this block
     *
     * @return z-coordinate
     */
    int getZ();

    // Paper start
    /**
     * Returns this block's coordinates packed into a long value.
     * Computed via: {@code Block.getBlockKey(this.getX(), this.getY(), this.getZ())}
     * @see Block#getBlockKey(int, int, int)
     * @return This block's x, y, and z coordinates packed into a long value
     * @deprecated see {@link #getBlockKey(int, int, int)}
     */
    @Deprecated(since = "1.18.1")
    public default long getBlockKey() {
        return Block.getBlockKey(this.getX(), this.getY(), this.getZ());
    }

    /**
     * Returns the specified block coordinates packed into a long value
     * <p>
     * The return value can be computed as follows:
     * <br>
     * {@code long value = ((long)x & 0x7FFFFFF) | (((long)z & 0x7FFFFFF) << 27) | ((long)y << 54);}
     * </p>
     *
     * <p>
     * And may be unpacked as follows:
     * <br>
     * {@code int x = (int) ((packed << 37) >> 37);}
     * <br>
     * {@code int y = (int) (packed >> 54);}
     * <br>
     * {@code int z = (int) ((packed << 10) >> 37);}
     * </p>
     *
     * @return This block's x, y, and z coordinates packed into a long value
     * @deprecated only encodes y block ranges from -512 to 511 and represents an already changed implementation detail
     */
    @Deprecated(since = "1.18.1")
    public static long getBlockKey(int x, int y, int z) {
        return ((long)x & 0x7FFFFFF) | (((long)z & 0x7FFFFFF) << 27) | ((long)y << 54);
    }

    /**
     * Returns the x component from the packed value.
     * @param packed The packed value, as computed by {@link Block#getBlockKey(int, int, int)}
     * @see Block#getBlockKey(int, int, int)
     * @return The x component from the packed value.
     * @deprecated see {@link #getBlockKey(int, int, int)}
     */
    @Deprecated(since = "1.18.1")
    public static int getBlockKeyX(long packed) {
        return (int) ((packed << 37) >> 37);
    }

    /**
     * Returns the y component from the packed value.
     * @param packed The packed value, as computed by {@link Block#getBlockKey(int, int, int)}
     * @see Block#getBlockKey(int, int, int)
     * @return The y component from the packed value.
     * @deprecated see {@link #getBlockKey(int, int, int)}
     */
    @Deprecated(since = "1.18.1")
    public static int getBlockKeyY(long packed) {
        return (int) (packed >> 54);
    }

    /**
     * Returns the z component from the packed value.
     * @param packed The packed value, as computed by {@link Block#getBlockKey(int, int, int)}
     * @see Block#getBlockKey(int, int, int)
     * @return The z component from the packed value.
     * @deprecated see {@link #getBlockKey(int, int, int)}
     */
    @Deprecated(since = "1.18.1")
    public static int getBlockKeyZ(long packed) {
        return (int) ((packed << 10) >> 37);
    }
    // Paper end

    // Paper start - add isValidTool
    /**
     * Checks if the itemstack is a valid tool to
     * break the block with
     *
     * @param itemStack The (tool) itemstack
     * @return whether the block will drop items
     * @deprecated partially replaced by {@link Block#isPreferredTool(ItemStack)}
     */
    @Deprecated(since = "1.21", forRemoval = true) // Paper
    boolean isValidTool(@NotNull ItemStack itemStack);
    // Paper end - add isValidTool

    /**
     * Gets the Location of the block
     *
     * @return Location of block
     */
    @NotNull
    Location getLocation();

    /**
     * Stores the location of the block in the provided Location object.
     * <p>
     * If the provided Location is null this method does nothing and returns
     * null.
     *
     * @param loc the location to copy into
     * @return The Location object provided or null
     */
    @Contract("null -> null; !null -> !null")
    @Nullable
    Location getLocation(@Nullable Location loc);

    /**
     * Gets the chunk which contains this block
     *
     * @return Containing Chunk
     */
    @NotNull
    Chunk getChunk();

    /**
     * Sets the complete data for this block
     *
     * @param data new block specific data
     */
    void setBlockData(@NotNull BlockData data);

    /**
     * Sets the complete data for this block
     *
     * <br>
     * Note that applyPhysics = false is not in general safe. It should only be
     * used when you need to avoid triggering a physics update of neighboring
     * blocks, for example when creating a {@link Bisected} block. If you are
     * using a custom populator, then this parameter may also be required to
     * prevent triggering infinite chunk loads on border blocks. This method
     * should NOT be used to "hack" physics by placing blocks in impossible
     * locations. Such blocks are liable to be removed on various events such as
     * world upgrades. Furthermore setting large amounts of such blocks in close
     * proximity may overload the server physics engine if an update is
     * triggered at a later point. If this occurs, the resulting behavior is
     * undefined.
     *
     * @param data new block specific data
     * @param applyPhysics false to cancel physics from the changed block
     */
    void setBlockData(@NotNull BlockData data, boolean applyPhysics);

    /**
     * Sets the type of this block
     *
     * @param type Material to change this block to
     */
    void setType(@NotNull Material type);

    /**
     * Sets the type of this block
     *
     * <br>
     * Note that applyPhysics = false is not in general safe. It should only be
     * used when you need to avoid triggering a physics update of neighboring
     * blocks, for example when creating a {@link Bisected} block. If you are
     * using a custom populator, then this parameter may also be required to
     * prevent triggering infinite chunk loads on border blocks. This method
     * should NOT be used to "hack" physics by placing blocks in impossible
     * locations. Such blocks are liable to be removed on various events such as
     * world upgrades. Furthermore setting large amounts of such blocks in close
     * proximity may overload the server physics engine if an update is
     * triggered at a later point. If this occurs, the resulting behavior is
     * undefined.
     *
     * @param type Material to change this block to
     * @param applyPhysics False to cancel physics on the changed block.
     */
    void setType(@NotNull Material type, boolean applyPhysics);

    /**
     * Gets the face relation of this block compared to the given block.
     * <p>
     * For example:
     * <pre>{@code
     * Block current = world.getBlockAt(100, 100, 100);
     * Block target = world.getBlockAt(100, 101, 100);
     *
     * current.getFace(target) == BlockFace.Up;
     * }</pre>
     * <br>
     * If the given block is not connected to this block, null may be returned
     *
     * @param block Block to compare against this block
     * @return BlockFace of this block which has the requested block, or null
     */
    @Nullable
    BlockFace getFace(@NotNull Block block);

    /**
     * Captures the current state of this block. You may then cast that state
     * into any accepted type, such as Furnace or Sign.
     * <p>
     * The returned object will never be updated, and you are not guaranteed
     * that (for example) a sign is still a sign after you capture its state.
     *
     * @return BlockState with the current state of this block.
     */
    @NotNull
    BlockState getState();

    // Paper start
    /**
     * @see #getState() optionally disables use of snapshot, to operate on real block data
     * @param useSnapshot if this block is a TE, should we create a fully copy of the TileEntity
     * @return BlockState with the current state of this block
     */
    @NotNull
    BlockState getState(boolean useSnapshot);
    // Paper end

    /**
     * Returns the biome that this block resides in
     *
     * @return Biome type containing this block
     * @see #getComputedBiome()
     */
    @NotNull
    Biome getBiome();

    // Paper start
    /**
     * Gets the computed biome at the location of this Block.
     *
     * @return computed biome at the location of this Block.
     * @see org.bukkit.RegionAccessor#getComputedBiome(int, int, int)
     */
    @NotNull
    Biome getComputedBiome();
    // Paper end

    /**
     * Sets the biome that this block resides in
     *
     * @param bio new Biome type for this block
     */
    void setBiome(@NotNull Biome bio);

    /**
     * Returns true if the block is being powered by Redstone.
     *
     * @return True if the block is powered.
     */
    boolean isBlockPowered();

    /**
     * Returns true if the block is being indirectly powered by Redstone.
     *
     * @return True if the block is indirectly powered.
     */
    boolean isBlockIndirectlyPowered();

    /**
     * Returns true if the block face is being powered by Redstone.
     *
     * @param face The block face
     * @return True if the block face is powered.
     */
    boolean isBlockFacePowered(@NotNull BlockFace face);

    /**
     * Returns true if the block face is being indirectly powered by Redstone.
     *
     * @param face The block face
     * @return True if the block face is indirectly powered.
     */
    boolean isBlockFaceIndirectlyPowered(@NotNull BlockFace face);

    /**
     * Returns the redstone power being provided to this block face
     *
     * @param face the face of the block to query or BlockFace.SELF for the
     *     block itself
     * @return The power level.
     */
    int getBlockPower(@NotNull BlockFace face);

    /**
     * Returns the redstone power being provided to this block
     *
     * @return The power level.
     */
    int getBlockPower();

    /**
     * Checks if this block is empty.
     * <p>
     * A block is considered empty when {@link #getType()} returns {@link
     * Material#AIR}.
     *
     * @return true if this block is empty
     */
    boolean isEmpty();

    /**
     * Checks if this block is liquid.
     * <p>
     * A block is considered liquid when {@link #getType()} returns {@link
     * Material#WATER} or {@link Material#LAVA}.
     *
     * @return true if this block is liquid
     */
    boolean isLiquid();

    // Paper start
    /**
     * Check if this block is solid
     * <p>
     * Determined by Minecraft, typically a block a player can use to place a new block to build things.
     * An example of a non buildable block would be liquids, flowers, or fire
     *
     * @return true if block is buildable
     */
    boolean isBuildable();
    /**
     * Check if this block is burnable
     * <p>
     * Determined by Minecraft, typically a block that fire can destroy (Wool, Wood)
     *
     * @return true if block is burnable
     */
    boolean isBurnable();
    /**
     * Check if this block is replaceable
     * <p>
     * Determined by Minecraft, representing a block that is not AIR that you can still place a new block at, such as flowers.
     * @return true if block is replaceable
     */
    boolean isReplaceable();
    /**
     * Check if this block is solid
     * <p>
     * Determined by Minecraft, typically a block a player can stand on and can't be passed through.
     *
     * This API is faster than accessing Material#isSolid as it avoids a material lookup and switch statement.
     * @return true if block is solid
     */
    boolean isSolid();

    /**
     * Checks if this block is collidable.
     *
     * @return true if collidable
     */
    boolean isCollidable();
    // Paper end

    /**
     * Gets the temperature of this block.
     *
     * @return Temperature of this block
     */
    double getTemperature();

    /**
     * Gets the humidity of the biome of this block
     *
     * @return Humidity of this block
     */
    double getHumidity();

    /**
     * Returns the reaction of the block when moved by a piston
     *
     * @return reaction
     */
    @NotNull
    PistonMoveReaction getPistonMoveReaction();

    /**
     * Breaks the block and spawns items as if a player had digged it regardless
     * of the tool.
     *
     * @return true if the block was destroyed
     */
    boolean breakNaturally();

    /**
     * Breaks the block and spawns items as if a player had digged it with a
     * specific tool
     *
     * @param tool The tool or item in hand used for digging
     * @return true if the block was destroyed
     */
    boolean breakNaturally(@Nullable ItemStack tool);

    // Paper start
    /**
     * Breaks the block and spawns item drops as if a player had broken it
     *
     * @param triggerEffect Play the block break particle effect and sound
     * @return true if the block was destroyed
     * @see #breakNaturally(boolean, boolean) to trigger exp drops
     */
    default boolean breakNaturally(boolean triggerEffect) {
        return this.breakNaturally(triggerEffect, false);
    }

    /**
     * Breaks the block and spawns item drops as if a player had broken it
     *
     * @param triggerEffect Play the block break particle effect and sound
     * @param dropExperience drop exp if the block normally does so
     * @return true if the block was destroyed
     */
    boolean breakNaturally(boolean triggerEffect, boolean dropExperience);

    /**
     * Breaks the block and spawns item drops as if a player had broken it
     * with a specific tool
     *
     * @param tool The tool or item in hand used for digging
     * @param triggerEffect Play the block break particle effect and sound
     * @return true if the block was destroyed
     * @see #breakNaturally(ItemStack, boolean, boolean) to trigger exp drops
     */
    default boolean breakNaturally(@NotNull ItemStack tool, boolean triggerEffect) {
        return this.breakNaturally(tool, triggerEffect, false);
    }

    /**
     * Breaks the block and spawns item drops as if a player had broken it
     * with a specific tool
     *
     * @param tool The tool or item in hand used for digging
     * @param triggerEffect Play the block break particle effect and sound
     * @param dropExperience drop exp if the block normally does so
     * @return true if the block was destroyed
     */
    boolean breakNaturally(@NotNull ItemStack tool, boolean triggerEffect, boolean dropExperience);

    /**
     * Causes the block to be ticked, this is different from {@link Block#randomTick()},
     * in that it is usually scheduled to occur, for example
     * redstone components being activated, sand falling, etc.
     * <p>
     * This method may directly fire events relating to block ticking.
     *
     * @see #fluidTick()
     */
    void tick();

    /**
     * Causes the fluid to be ticked, this is different from {@link Block#randomTick()},
     * in that it is usually scheduled to occur, for example
     * causing waterlogged blocks to spread.
     * <p>
     * This method may directly fire events relating to fluid ticking.
     *
     * @see #tick()
     */
    void fluidTick();

    /**
     * Causes the block to be ticked randomly.
     * This has a chance to execute naturally if {@link BlockData#isRandomlyTicked()} is true.
     * <p>
     * For certain blocks, this behavior may be the same as {@link Block#tick()}.
     * <p>
     * This method may directly fire events relating to block random ticking.
     *
     * @see #tick()
     * @see #fluidTick()
     */
    void randomTick();
    // Paper end

    /**
     * Simulate bone meal application to this block (if possible).
     *
     * @param face the face on which bonemeal should be applied
     *
     * @return true if the block was bonemealed, false otherwise
     */
    boolean applyBoneMeal(@NotNull BlockFace face);

    /**
     * Returns a list of items which could drop by destroying this block.
     * <p>
     * The items are not guaranteed to be consistent across multiple calls to this
     * method as this just uses the block type's loot table.
     *
     * @return a list of dropped items for this type of block
     */
    @NotNull
    Collection<ItemStack> getDrops();

    /**
     * Returns a list of items which could drop by destroying this block with
     * a specific tool.
     * <p>
     * The items are not guaranteed to be consistent across multiple calls to this
     * method as this just uses the block type's loot table.
     *
     * @param tool The tool or item in hand used for digging
     * @return a list of dropped items for this type of block
     */
    @NotNull
    Collection<ItemStack> getDrops(@Nullable ItemStack tool);

    /**
     * Returns a list of items which could drop by the entity destroying this
     * block with a specific tool.
     * <p>
     * The items are not guaranteed to be consistent across multiple calls to this
     * method as this just uses the block type's loot table.
     *
     * @param tool The tool or item in hand used for digging
     * @param entity the entity destroying the block
     * @return a list of dropped items for this type of block
     */
    @NotNull
    Collection<ItemStack> getDrops(@Nullable ItemStack tool, @Nullable Entity entity); // Paper

    /**
     * Returns if the given item is a preferred choice to break this Block.
     *
     * In some cases this determines if a block will drop anything or extra
     * loot.
     *
     * @param tool The tool or item used for breaking this block
     * @return true if the tool is preferred for breaking this block.
     */
    boolean isPreferredTool(@NotNull ItemStack tool);

    /**
     * Gets the speed at which the given player would break this block, taking
     * into account tools, potion effects, whether or not the player is in
     * water, enchantments, etc.
     *
     * The returned value is the amount of progress made in breaking the block
     * each tick. When the total breaking progress reaches {@code 1.0f}, the
     * block is broken. Note that the break speed can change in the course of
     * breaking a block, e.g. if a potion effect is applied or expires, or the
     * player jumps/enters water.
     *
     * @param player player breaking the block
     * @return the speed at which the player breaks this block
     */
    float getBreakSpeed(@NotNull Player player);

    /**
     * Checks if this block is passable.
     * <p>
     * A block is passable if it has no colliding parts that would prevent
     * players from moving through it.
     * <p>
     * Examples: Tall grass, flowers, signs, etc. are passable, but open doors,
     * fence gates, trap doors, etc. are not because they still have parts that
     * can be collided with.
     *
     * @return <code>true</code> if passable
     */
    boolean isPassable();

    /**
     * Performs a ray trace that checks for collision with this specific block
     * in its current state using its precise collision shape.
     *
     * @param start the start location
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @param fluidCollisionMode the fluid collision mode
     * @return the ray trace hit result, or <code>null</code> if there is no hit
     */
    @Nullable
    RayTraceResult rayTrace(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode);

    /**
     * Gets the approximate bounding box for this block.
     * <p>
     * This isn't exact as some blocks {@link org.bukkit.block.data.type.Stairs}
     * contain many bounding boxes to establish their complete form.
     *
     * Also, the box may not be exactly the same as the collision shape (such as
     * cactus, which is 16/16 of a block with 15/16 collisional bounds).
     *
     * This method will return an empty bounding box if the geometric shape of
     * the block is empty (such as air blocks).
     *
     * @return the approximate bounding box of the block
     */
    @NotNull
    BoundingBox getBoundingBox();

    /**
     * Gets the collision shape of this block.
     *
     * @return a {@link VoxelShape} representing the collision shape of this
     * block.
     */
    @NotNull
    VoxelShape getCollisionShape();

    /**
     * Checks if this block is a valid placement location for the specified
     * block data.
     *
     * @param data the block data to check
     * @return <code>true</code> if the block data can be placed here
     */
    boolean canPlace(@NotNull BlockData data);

    // Paper start
    /**
     * Gets the {@link com.destroystokyo.paper.block.BlockSoundGroup} for this block.
     * <p>
     * This object contains the block, step, place, hit, and fall sounds.
     *
     * @return the sound group for this block
     * @deprecated use {@link #getBlockSoundGroup()}
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.18.2")
    com.destroystokyo.paper.block.BlockSoundGroup getSoundGroup();

    /**
     * Gets the {@link org.bukkit.SoundGroup} for this block.
     *
     * @return the sound group for this block
     */
    @NotNull org.bukkit.SoundGroup getBlockSoundGroup();

    /**
     * @deprecated use {@link #translationKey()}
     */
    @NotNull
    @Deprecated(forRemoval = true)
    String getTranslationKey();
    // Paper end

    // Paper start - destroy speed API
    /**
     * Gets the speed at which this block will be destroyed by a given {@link ItemStack}
     * <p>
     * Default value is 1.0
     *
     * @param itemStack {@link ItemStack} used to mine this Block
     * @return the speed that this Block will be mined by the given {@link ItemStack}
     */
    default float getDestroySpeed(final @NotNull ItemStack itemStack) {
        return this.getBlockData().getDestroySpeed(itemStack);
    }

    /**
     * Gets the speed at which this block will be destroyed by a given {@link ItemStack}
     * <p>
     * Default value is 1.0
     *
     * @param itemStack {@link ItemStack} used to mine this Block
     * @param considerEnchants true to look at enchants on the itemstack
     * @return the speed that this Block will be mined by the given {@link ItemStack}
     */
    default float getDestroySpeed(@NotNull ItemStack itemStack, boolean considerEnchants) {
        return this.getBlockData().getDestroySpeed(itemStack, considerEnchants);
    }
    // Paper end - destroy speed API
}
