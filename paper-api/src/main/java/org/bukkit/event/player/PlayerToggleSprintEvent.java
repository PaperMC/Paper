package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player toggles their sprinting state
 *
 * @since 1.0.0 R1
 */
public class PlayerToggleSprintEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final boolean isSprinting;
    private boolean cancel = false;

    public PlayerToggleSprintEvent(@NotNull final Player player, final boolean isSprinting) {
        super(player);
        this.isSprinting = isSprinting;
    }

    /**
     * Gets whether the player is now sprinting or not.
     *
     * @return sprinting state
     */
    public boolean isSprinting() {
        return isSprinting;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
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
