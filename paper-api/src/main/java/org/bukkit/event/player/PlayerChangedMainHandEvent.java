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
    private final MainHand mainHand;

    public PlayerChangedMainHandEvent(@NotNull Player who, @NotNull MainHand mainHand) {
        super(who);
        this.mainHand = mainHand;
    }

    /**
     * Gets the new main hand of the player. The old hand is still momentarily
     * available via {@link Player#getMainHand()}.
     *
     * @return the new {@link MainHand} of the player
     */
    @NotNull
    public MainHand getMainHand() {
        return mainHand;
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
