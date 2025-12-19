package org.bukkit.block.data;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.SoundGroup;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.BlockSupport;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockData extends Cloneable {

    /**
     * Get the Material represented by this block data.
     *
     * @return the material
     */
    @NotNull
    Material getMaterial();

    /**
     * Gets a string, which when passed into a method such as
     * {@link Server#createBlockData(java.lang.String)} will unambiguously
     * recreate this instance.
     *
     * @return serialized data string for this block
     */
    @NotNull
    String getAsString();

    /**
     * Gets a string, which when passed into a method such as
     * {@link Server#createBlockData(java.lang.String)} will recreate this or a
     * similar instance where unspecified states (if any) may be optionally
     * omitted. If this instance was parsed and states are omitted, this exact
     * instance will be creatable when parsed again, else their equality cannot
     * be guaranteed.
     * <p>
     * This method will only take effect for BlockData instances created by
     * methods such as {@link Server#createBlockData(String)} or any similar
     * method whereby states are optionally defined. If otherwise, the result of
     * {@link #getAsString()} will be returned. The following behaviour would be
     * expected:
     * <pre>{@code
     * String dataString = "minecraft:chest[waterlogged=true]"
     * BlockData data = Bukkit.createBlockData(dataString);
     * dataString.equals(data.getAsString(true)); // This would return true
     * dataString.equals(data.getAsString(false)); // This would return false as all states are present
     * dataString.equals(data.getAsString()); // This is equivalent to the above, "getAsString(false)"
     * }</pre>
     *
     * @param hideUnspecified true if unspecified states should be omitted,
     * false if they are to be shown as performed by {@link #getAsString()}.
     *
     * @return serialized data string for this block
     */
    @NotNull
    String getAsString(boolean hideUnspecified);

    /**
     * Merges all explicitly set states from the given data with this BlockData.
     * <br>
     * Note that the given data MUST have been created from one of the String
     * parse methods, e.g. {@link Server#createBlockData(java.lang.String)} and
     * not have been subsequently modified.
     * <br>
     * Note also that the block types must match identically.
     *
     * @param data the data to merge from
     * @return a new instance of this blockdata with the merged data
     */
    @NotNull
    BlockData merge(@NotNull BlockData data);

    /**
     * Checks if the specified BlockData matches this block data.
     * <br>
     * The semantics of this method are such that for manually created or
     * modified BlockData it has the same effect as
     * {@link Object#equals(java.lang.Object)}, whilst for parsed data (that to
     * which {@link #merge(org.bukkit.block.data.BlockData)} applies), it will
     * return true when the type and all explicitly set states match.
     * <br>
     * <b>Note that these semantics mean that a.matches(b) may not be the same
     * as b.matches(a)</b>
     *
     * @param data the data to match against (normally a parsed constant)
     * @return if there is a match
     */
    boolean matches(@Nullable BlockData data);

    /**
     * Returns a copy of this BlockData.
     *
     * @return a copy of the block data
     */
    @NotNull
    BlockData clone();

    /**
     * Gets the block's {@link SoundGroup} which can be used to get its step
     * sound, hit sound, and others.
     *
     * @return the sound effect group
     */
    @NotNull
    SoundGroup getSoundGroup();

    /**
     * Get the amount of light emitted by this state when in the world.
     *
     * @return the light emission
     */
    int getLightEmission();

    /**
     * Check whether or not this state will occlude other blocks.
     * <p>
     * Block state occlusion affects visual features of other blocks (e.g. leaves and
     * wet sponges will not spawn dripping water particles if an occluding state is
     * below it), or whether light will pass through it.
     *
     * @return true if occluding, false otherwise
     */
    boolean isOccluding();

    /**
     * Check whether or not this state requires a specific item to be used to drop
     * items when broken. For example, diamond ore requires an iron pickaxe and will
     * not drop diamonds when broken with a wooden or stone pickaxe.
     *
     * @return true if a more specific item is required for drops, false if any item
     * (or an empty hand) will drop items
     */
    boolean requiresCorrectToolForDrops();

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
     * Returns the reaction of the block when moved by a piston
     *
     * @return reaction
     */
    @NotNull
    PistonMoveReaction getPistonMoveReaction();

    /**
     * Checks if this state would be properly supported if it were placed at
     * the given {@link Block}.
     * <p>
     * This may be useful, for instance, to check whether or not a wall torch is
     * capable of surviving on its neighbouring block states.
     *
     * @param block the block position at which the state would be placed
     * @return true if the block is supported, false if this state would not survive
     * the world conditions
     */
    boolean isSupported(@NotNull Block block);

    /**
     * Checks if this state would be properly supported if it were placed at
     * the block at the given {@link Location}.
     * <p>
     * This may be useful, for instance, to check whether or not a wall torch is
     * capable of surviving on its neighbouring block states.
     *
     * @param location the location at which the state would be placed
     *
     * @return true if the block is supported, false if this state would not survive
     * the world conditions
     */
    boolean isSupported(@NotNull Location location);

    /**
     * Checks if a state's {@link BlockFace} is capable of providing a given level
     * of {@link BlockSupport} for neighbouring block states.
     * <p>
     * Any given state may support either none, one, or more than one level of block
     * support depending on its states. A common example would be a wall's ability to support
     * torches only on the center of the upper block face, whereas a grass block would
     * support all levels of block support on all block faces.
     *
     * @param face the face to check
     * @param support the possible support level
     *
     * @return true if the face is sturdy and can support a block, false otherwise
     */
    boolean isFaceSturdy(@NotNull BlockFace face, @NotNull BlockSupport support);

    // Paper start
    /**
     * Calculates the collision shape this block data would have at a particular location.
     * <p>
     * This does not take into account any block updates that may occur if the block was to be actually placed in the world.
     *
     * @param location the location to calculate the collision shape at
     *
     * @return a {@link org.bukkit.util.VoxelShape} representing the collision shape of this block data.
     */
    @NotNull org.bukkit.util.VoxelShape getCollisionShape(@NotNull Location location);
    // Paper end

    /**
     * Gets the color this block should appear as when rendered on a map.
     *
     * @return the color associated with this BlockData
     */
    @NotNull
    Color getMapColor();

    /**
     * Gets the material that a player would use to place this block.
     * <p>
     * For most blocks this is the same as {@link #getMaterial()} but some blocks
     * have different materials used to place them.
     *
     * For example:
     * <pre>
     * {@link Material#REDSTONE_WIRE} -> {@link Material#REDSTONE}
     * {@link Material#CARROTS} -> {@link Material#CARROT}
     * </pre>
     * @return placement material or {@link Material#AIR} if it doesn't have one
     */
    @NotNull
    Material getPlacementMaterial();

    /**
     * Rotates this blockdata by the specified {@link StructureRotation}.
     * <p>
     * This has no effect on blocks that do not have any rotatable states.
     *
     * @param rotation the rotation
     */
    void rotate(@NotNull StructureRotation rotation);

    /**
     * Mirrors this blockdata using the specified {@link Mirror}.
     * <p>
     * This has no effect on blocks that do not have any mirrorable states.
     *
     * @param mirror the mirror
     */
    void mirror(@NotNull Mirror mirror);

    /**
     * Copies all applicable properties from this BlockData to the provided
     * BlockData.
     * <p>
     * Only modifies properties that both blocks share in common.
     *
     * @param other the BlockData to copy properties to
     */
    void copyTo(@NotNull BlockData other);

    /**
     * Creates a new default {@link BlockState} for this type of Block, not
     * bound to a location.
     *
     * @return a new {@link BlockState}
     */
    @NotNull
    BlockState createBlockState();

    // Paper start - destroy speed API
    /**
     * Gets the speed at which this block will be destroyed by a given {@link ItemStack}
     * <p>
     * Default value is 1.0
     *
     * @param itemStack {@link ItemStack} used to mine this Block
     * @return the speed that this Block will be mined by the given {@link ItemStack}
     * @apiNote this method assumes default player state and hence, e.g., does not take into account changed
     * player attributes or potion effects.
     */
    default float getDestroySpeed(final @NotNull ItemStack itemStack) {
        return this.getDestroySpeed(itemStack, false);
    }

    /**
     * Gets the speed at which this block will be destroyed by a given {@link ItemStack}
     * <p>
     * Default value is 1.0
     *
     * @param itemStack {@link ItemStack} used to mine this Block
     * @param considerEnchants true to look at enchants on the itemstack
     * @return the speed that this Block will be mined by the given {@link ItemStack}
     * @apiNote this method assumes default player state and hence, e.g., does not take into account changed
     * player attributes or potion effects.
     */
    float getDestroySpeed(@NotNull ItemStack itemStack, boolean considerEnchants);
    // Paper end - destroy speed API

    // Paper start - Tick API
    /**
     * Gets if this block is ticked randomly in the world.
     * The blocks current state may change this value.
     *
     * @return is ticked randomly
     */
    boolean isRandomlyTicked();
    // Paper end - Tick API

    /**
     * Checks if this block can be immediately replaced by another block, such as placing a new block in air or tall grass.
     * @return true if block is replaceable
     */
    boolean isReplaceable();
}
