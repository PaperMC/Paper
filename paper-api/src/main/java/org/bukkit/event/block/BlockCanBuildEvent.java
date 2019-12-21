package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when we try to place a block, to see if we can build it here or not.
 * <p>
 * Note:
 * <ul>
 * <li>The Block returned by getBlock() is the block we are trying to place
 *     on, not the block we are trying to place.
 * <li>If you want to figure out what is being placed, use {@link
 *     #getMaterial()} instead.
 * </ul>
 */
public class BlockCanBuildEvent extends BlockEvent {
    private static final HandlerList handlers = new HandlerList();
    protected boolean buildable;

    protected BlockData blockData;
    private final Player player;

    @Deprecated
    public BlockCanBuildEvent(@NotNull final Block block, @NotNull final BlockData type, final boolean canBuild) {
        this(block, null, type, canBuild);
    }

    /**
     * @param block the block involved in this event
     * @param player the player placing the block
     * @param type the id of the block to place
     * @param canBuild whether we can build
     */
    public BlockCanBuildEvent(@NotNull final Block block, @Nullable final Player player, @NotNull final BlockData type, final boolean canBuild) {
        super(block);
        this.player = player;
        this.buildable = canBuild;
        this.blockData = type;
    }

    /**
     * Gets whether or not the block can be built here.
     * <p>
     * By default, returns Minecraft's answer on whether the block can be
     * built here or not.
     *
     * @return boolean whether or not the block can be built
     */
    public boolean isBuildable() {
        return buildable;
    }

    /**
     * Sets whether the block can be built here or not.
     *
     * @param cancel true if you want to allow the block to be built here
     *     despite Minecraft's default behaviour
     */
    public void setBuildable(boolean cancel) {
        this.buildable = cancel;
    }

    /**
     * Gets the Material that we are trying to place.
     *
     * @return The Material that we are trying to place
     */
    @NotNull
    public Material getMaterial() {
        return blockData.getMaterial();
    }

    /**
     * Gets the BlockData that we are trying to place.
     *
     * @return The BlockData that we are trying to place
     */
    @NotNull
    public BlockData getBlockData() {
        return blockData;
    }

    /**
     * Gets the player who placed the block involved in this event.
     * <br>
     * May be null for legacy calls of the event.
     *
     * @return The Player who placed the block involved in this event
     */
    @Nullable
    public Player getPlayer() {
        return player;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
