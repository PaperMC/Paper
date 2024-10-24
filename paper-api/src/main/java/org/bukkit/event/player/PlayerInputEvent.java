package org.bukkit.event.player;

import org.bukkit.Input;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a player sends updated input to the server.
 *
 * @see Player#getCurrentInput()
 */
@ApiStatus.Experimental
public class PlayerInputEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Input input;

    public PlayerInputEvent(@NotNull final Player player, @NotNull final Input input) {
        super(player);
        this.input = input;
    }

    /**
     * Gets the new input received from this player.
     *
     * @return the new input
     */
    @NotNull
    public Input getInput() {
        return input;
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
