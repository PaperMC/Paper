package io.papermc.paper.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a block entity update is too large to be sent to a player.
 * <p>
 *     This will be fired every time a chunk is re-sent to a player for each
 *     large block entity, be careful as to not run expensive operations.
 * </p>
 * <p>
 * This event is asynchronous and runs on the network thread.
 */
public class AsyncBlockEntityUpdateTooLargeEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Block block;

    @ApiStatus.Internal
    public AsyncBlockEntityUpdateTooLargeEvent(@NotNull final Block block, @NotNull final Player player) {
        super(player, true);
        this.block = block;
    }

    /**
     * Gets the block entity's block.
     *
     * @return the block
     */
    @NotNull
    public Block getBlock() {
        return this.block;
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
