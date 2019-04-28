package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player toggles their flying state
 */
public class PlayerToggleFlightEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final boolean isFlying;
    private boolean cancel = false;

    public PlayerToggleFlightEvent(@NotNull final Player player, final boolean isFlying) {
        super(player);
        this.isFlying = isFlying;
    }

    /**
     * Returns whether the player is trying to start or stop flying.
     *
     * @return flying state
     */
    public boolean isFlying() {
        return isFlying;
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
