package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when a player changes their currently held item
 */
public class PlayerItemHeldEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final int previous;
    private final int current;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerItemHeldEvent(@NotNull final Player player, final int previous, final int current) {
        super(player);
        this.previous = previous;
        this.current = current;
    }

    /**
     * Gets the previous held slot index
     *
     * @return Previous slot index
     */
    public int getPreviousSlot() {
        return this.previous;
    }

    /**
     * Gets the new held slot index
     *
     * @return New slot index
     */
    public int getNewSlot() {
        return this.current;
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
