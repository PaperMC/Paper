package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
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

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final org.bukkit.inventory.EquipmentSlot hand;
    protected BlockData blockData;
    protected boolean buildable;

    @Deprecated(since = "1.13.2", forRemoval = true)
    @ApiStatus.Internal
    public BlockCanBuildEvent(@NotNull final Block block, @NotNull final BlockData type, final boolean canBuild) {
        this(block, null, type, canBuild, org.bukkit.inventory.EquipmentSlot.HAND); // Paper - expose hand
    }

    @Deprecated(forRemoval = true)
    @ApiStatus.Internal
    public BlockCanBuildEvent(@NotNull final Block block, @Nullable final Player player, @NotNull final BlockData type, final boolean canBuild) {
        this(block, player, type, canBuild, org.bukkit.inventory.EquipmentSlot.HAND); // Paper start - expose hand
    }

    @ApiStatus.Internal
    public BlockCanBuildEvent(@NotNull final Block block, @Nullable final Player player, @NotNull final BlockData type, final boolean canBuild, @NotNull final org.bukkit.inventory.EquipmentSlot hand) { // Paper end - expose hand
        super(block);
        this.player = player;
        this.buildable = canBuild;
        this.blockData = type;
        this.hand = hand;
    }

    /**
     * Gets the player who placed the block involved in this event.
     * <br>
     * May be {@code null} for legacy calls of the event.
     *
     * @return The Player who placed the block involved in this event
     */
    @Nullable
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets the Material that we are trying to place.
     *
     * @return The Material that we are trying to place
     */
    @NotNull
    public Material getMaterial() {
        return this.blockData.getMaterial();
    }

    /**
     * Gets the BlockData that we are trying to place.
     *
     * @return The BlockData that we are trying to place
     */
    @NotNull
    public BlockData getBlockData() {
        return this.blockData.clone();
    }

    /**
     * Gets the hand the player will use to place the block
     *
     * @return the EquipmentSlot representing the players hand.
     */
    @NotNull
    public org.bukkit.inventory.EquipmentSlot getHand() {
        return this.hand;
    }

    /**
     * Gets whether the block can be built here.
     * <p>
     * By default, returns Minecraft's answer on whether the block can be
     * built here or not.
     *
     * @return boolean whether the block can be built
     */
    public boolean isBuildable() {
        return this.buildable;
    }

    /**
     * Sets whether the block can be built here or not.
     *
     * @param cancel {@code true} if you want to allow the block to be built here
     *     despite Minecraft's default behaviour
     */
    public void setBuildable(boolean cancel) {
        this.buildable = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
