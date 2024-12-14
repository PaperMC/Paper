package org.bukkit.event.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called after a block is broken by a player and potential drops are computed, even if said blocks loot table
 * does not define any drops at the point the event is constructed.
 *
 * If the block break is cancelled, this event won't be called.
 *
 * If isDropItems in {@link org.bukkit.event.block.BlockBreakEvent} is set to false, this event won't be
 * called.
 *
 * If a block is broken and isDropItems is set to true, this event will be called even if the block does
 * not drop any items, for example glass broken by hand. In this case, #getItems() will be empty.
 *
 * This event will also be called if the player breaks a multi block structure,
 * for example a torch on top of a stone. Both items will be included in the #getItems() list.
 *
 * The Block is already broken as this event is called, so #getBlock() will be
 * AIR in most cases. Use #getBlockState() for more Information about the broken
 * block.
 *
 * @since 1.13.2
 */
public class BlockDropItemEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private boolean cancel;
    private final BlockState blockState;
    private final List<Item> items;

    public BlockDropItemEvent(@NotNull Block block, @NotNull BlockState blockState, @NotNull Player player, @NotNull List<Item> items) {
        super(block);
        this.blockState = blockState;
        this.player = player;
        this.items = items;
    }

    /**
     * Gets the Player that is breaking the block involved in this event.
     *
     * @return The Player that is breaking the block involved in this event
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the BlockState of the block involved in this event before it was
     * broken.
     *
     * @return The BlockState of the block involved in this event
     */
    @NotNull
    public BlockState getBlockState() {
        return blockState;
    }

    /**
     * Gets list of the Item drops caused by the block break.
     *
     * This list is mutable - removing an item from it will cause it to not
     * drop. Adding to the list is allowed.
     *
     * @return The Item the block caused to drop
     */
    @NotNull
    public List<Item> getItems() {
        return items;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
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
