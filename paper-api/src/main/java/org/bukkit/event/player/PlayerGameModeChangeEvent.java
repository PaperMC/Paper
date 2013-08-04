package org.bukkit.event.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when the GameMode of the player is changed.
 */
public class PlayerGameModeChangeEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final GameMode newGameMode;

    public PlayerGameModeChangeEvent(final Player player, final GameMode newGameMode) {
        super(player);
        this.newGameMode = newGameMode;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Gets the GameMode the player is switched to.
     *
     * @return  player's new GameMode
     */
    public GameMode getNewGameMode() {
        return newGameMode;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
