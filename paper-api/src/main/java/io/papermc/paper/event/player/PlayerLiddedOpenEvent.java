package io.papermc.paper.event.player;

import io.papermc.paper.block.LidMode;
import io.papermc.paper.block.LidState;
import io.papermc.paper.block.Lidded;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player opens a {@link Lidded} block.
 *
 * <p>
 * This is called every time a player opens a {@link Lidded} block
 * regardless of if the lid is already open (e.g. multiple players).
 * <p>
 * Cancelling this event prevents the player from being considered in other {@link Lidded} methods:
 * they will not contribute to the {@link Lidded#getTrueLidState()} and {@link Lidded#getEffectiveLidState()}.
 * <p>
 * This event is called twice for double chests, once for each half.
 */
@NullMarked
public class PlayerLiddedOpenEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Lidded blockState;
    private final Block block;
    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerLiddedOpenEvent(final @NotNull Player who, final @NotNull Lidded blockState, final @NotNull Block block) {
        super(who);
        this.cancelled = false;
        this.blockState = blockState;
        this.block = block;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Gets the {@link Lidded} block involved in this event.
     * @return the lidded block
     */
    @NotNull
    public Lidded getLidded() {
        return blockState;
    }

    /**
     * Gets the block involved in this event.
     * @return the block
     */
    @NotNull
    public Block getBlock() {
        return block;
    }

    /**
     * Gets if the block would appear to open, if this event is not cancelled.
     * return if the block would appear to open
     */
    public boolean isOpening() {
        return blockState.getLidMode() == LidMode.DEFAULT && blockState.getTrueLidState() == LidState.CLOSED;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
