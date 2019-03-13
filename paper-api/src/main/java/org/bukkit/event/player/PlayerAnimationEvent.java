package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

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
    public PlayerAnimationEvent(@NotNull final Player player) {
        super(player);

        // Only supported animation type for now:
        animationType = PlayerAnimationType.ARM_SWING;
    }

    /**
     * Get the type of this animation event
     *
     * @return the animation type
     */
    @NotNull
    public PlayerAnimationType getAnimationType() {
        return animationType;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
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
