package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Called when a player toggles their sneaking state
 */
public class PlayerToggleSneakEvent extends PlayerEvent implements Cancellable {
    private boolean isSneaking;
    private boolean cancel = false;

    public PlayerToggleSneakEvent(final Player player, boolean isSneaking) {
        super(Type.PLAYER_TOGGLE_SNEAK, player);
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
}
