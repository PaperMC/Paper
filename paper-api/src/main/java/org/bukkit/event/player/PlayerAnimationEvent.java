package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Represents a player animation event
 */
public class PlayerAnimationEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final PlayerAnimationType animationType;
    private boolean isCancelled = false;

    /**
     * Construct a new PlayerAnimation event
     *
     * @param player The player instance
     */
    public PlayerAnimationEvent(final Player player) {
        super(player);

        // Only supported animation type for now:
        animationType = PlayerAnimationType.ARM_SWING;
    }

    /**
     * Get the type of this animation event
     *
     * @return the animation type
     */
    public PlayerAnimationType getAnimationType() {
        return animationType;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
