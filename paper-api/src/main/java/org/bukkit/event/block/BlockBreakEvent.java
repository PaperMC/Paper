package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a block is broken by a player.
 * <p>
 * If you wish to have the block drop experience, you must set the experience
 * value above 0. By default, experience will be set in the event if:
 * <ol>
 * <li>The player is not in creative or adventure mode
 * <li>The player can loot the block (ie: does not destroy it completely, by
 *     using the correct tool)
 * <li>The player does not have silk touch
 * <li>The block drops experience in vanilla Minecraft
 * </ol>
 * <p>
 * Note:
 * Plugins wanting to simulate a traditional block drop should set the block
 * to air and utilize their own methods for determining what the default drop
 * for the block being broken is and what to do about it, if anything.
 * <p>
 * If this event is cancelled, the block will not break and
 * experience will not drop.
 */
public class BlockBreakEvent extends BlockExpEvent implements Cancellable {

    private final Player player;
    private boolean dropItems;

    private boolean cancelled;

    @ApiStatus.Internal
    public BlockBreakEvent(@NotNull final Block block, @NotNull final Player player) {
        super(block, 0);

        this.player = player;
        this.dropItems = true; // Defaults to dropping items as it normally would
    }

    /**
     * Gets the Player that is breaking the block involved in this event.
     *
     * @return The Player that is breaking the block involved in this event
     */
    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Sets whether the block will attempt to drop items as it normally
     * would.
     * <p>
     * If and only if this is {@code false} then {@link BlockDropItemEvent} will not be
     * called after this event.
     *
     * @param dropItems Whether the block will attempt to drop items
     */
    public void setDropItems(boolean dropItems) {
        this.dropItems = dropItems;
    }

    /**
     * Gets whether the block will attempt to drop items.
     * <p>
     * If and only if this is {@code false} then {@link BlockDropItemEvent} will not be
     * called after this event.
     *
     * @return Whether the block will attempt to drop items
     */
    public boolean isDropItems() {
        return this.dropItems;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
