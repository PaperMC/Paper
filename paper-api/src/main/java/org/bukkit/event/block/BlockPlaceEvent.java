package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a block is placed by a player.
 * <p>
 * If this event is cancelled, the block will not be placed.
 */
public class BlockPlaceEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    protected Block placedAgainst;
    protected ItemStack itemInHand;
    protected Player player;
    protected BlockState replacedBlockState;
    protected boolean canBuild;
    protected EquipmentSlot hand;

    protected boolean cancelled;

    @ApiStatus.Internal
    @Deprecated(since = "1.9", forRemoval = true)
    public BlockPlaceEvent(@NotNull final Block placedBlock, @NotNull final BlockState replacedBlockState, @NotNull final Block placedAgainst, @NotNull final ItemStack itemInHand, @NotNull final Player thePlayer, final boolean canBuild) {
        this(placedBlock, replacedBlockState, placedAgainst, itemInHand, thePlayer, canBuild, EquipmentSlot.HAND);
    }

    @ApiStatus.Internal
    public BlockPlaceEvent(@NotNull final Block placedBlock, @NotNull final BlockState replacedBlockState, @NotNull final Block placedAgainst, @NotNull final ItemStack itemInHand, @NotNull final Player thePlayer, final boolean canBuild, @NotNull final EquipmentSlot hand) {
        super(placedBlock);
        this.placedAgainst = placedAgainst;
        this.itemInHand = itemInHand;
        this.player = thePlayer;
        this.replacedBlockState = replacedBlockState;
        this.canBuild = canBuild;
        this.hand = hand;
    }

    /**
     * Gets the block that this block was placed against
     *
     * @return Block the block that the new block was placed against
     */
    @NotNull
    public Block getBlockAgainst() {
        return this.placedAgainst;
    }

    /**
     * Gets the item in the player's hand when they placed the block.
     *
     * @return The ItemStack for the item in the player's hand when they
     *     placed the block
     */
    @NotNull
    public ItemStack getItemInHand() {
        return this.itemInHand;
    }

    /**
     * Gets the player who placed the block involved in this event.
     *
     * @return The Player who placed the block involved in this event
     */
    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Clarity method for getting the placed block. Not really needed except
     * for reasons of clarity.
     *
     * @return The Block that was placed
     */
    @NotNull
    public Block getBlockPlaced() {
        return this.getBlock();
    }

    /**
     * Gets the BlockState for the block which was replaced. Material type air
     * mostly.
     *
     * @return The BlockState for the block which was replaced.
     */
    @NotNull
    public BlockState getBlockReplacedState() {
        return this.replacedBlockState;
    }

    /**
     * Gets the value whether the player would be allowed to build here.
     * Defaults to {@code false} if the server was going to stop them (such as, the
     * player is in Spawn). Note that this is an entirely different check
     * than BLOCK_CANBUILD, as this refers to a player, not universe-physics
     * rule like cactus on dirt.
     *
     * @return boolean whether the server would allow a player to build here
     */
    public boolean canBuild() {
        return this.canBuild;
    }

    /**
     * Sets the canBuild state of this event. Set to {@code true} if you want the
     * player to be able to build.
     *
     * @param canBuild {@code true} if you want the player to be able to build
     */
    public void setBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }

    /**
     * Gets the hand which placed the block
     * @return Main or off-hand, depending on which hand was used to place the block
     */
    @NotNull
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
