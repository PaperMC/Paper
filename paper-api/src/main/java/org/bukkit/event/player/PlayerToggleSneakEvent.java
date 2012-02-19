package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a player toggles their sneaking state
 */
public class PlayerToggleSneakEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final boolean isSneaking;
    private boolean cancel = false;

    public PlayerToggleSneakEvent(final Player player, final boolean isSneaking) {
        super(player);
        this.isSneaking = isSneaking;
    }

    /**
     * Returns whether the player is now sneaking or not.
     *
     * @return sneaking state
     */
    public boolean isSneaking() {
        return isSneaking;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
