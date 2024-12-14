package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a player animation event
 * <br>Use {@link io.papermc.paper.event.player.PlayerArmSwingEvent} for determining which arm was swung.
 *
 * @since 1.0.0 R1
 */
public class PlayerAnimationEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final PlayerAnimationType animationType;
    private boolean isCancelled = false;

    @Deprecated(since = "1.19")
    public PlayerAnimationEvent(@NotNull final Player player) {
        this(player, PlayerAnimationType.ARM_SWING);
    }

    /**
     * Construct a new PlayerAnimation event
     *
     * @param player The player instance
     * @param playerAnimationType The animation type
     */
    public PlayerAnimationEvent(@NotNull final Player player, @NotNull final PlayerAnimationType playerAnimationType) {
        super(player);
        animationType = playerAnimationType;
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

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
