package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MainHand;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player changes their main hand in the client settings.
 */
public class PlayerChangedMainHandEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    //
    private final MainHand newMainHand;

    public PlayerChangedMainHandEvent(@NotNull Player who, @NotNull MainHand newMainHand) {
        super(who);
        this.newMainHand = newMainHand;
    }

    /**
     * Gets the old main hand of the player.
     *
     * @return the old {@link MainHand} of the player
     */
    @NotNull
    public MainHand getMainHand() {
        return newMainHand == MainHand.LEFT ? MainHand.RIGHT : MainHand.LEFT;
    }

    /**
     * Gets the new main hand of the player.
     *
     * @return the new {@link MainHand} of the player
     */
    @NotNull
    public MainHand getNewMainHand() {
        return newMainHand;
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
