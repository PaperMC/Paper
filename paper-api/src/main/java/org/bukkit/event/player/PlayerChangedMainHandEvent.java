package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MainHand;

/**
 * Called when a player changes their main hand in the client settings.
 */
public class PlayerChangedMainHandEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    //
    private final MainHand mainHand;

    public PlayerChangedMainHandEvent(Player who, MainHand mainHand) {
        super(who);
        this.mainHand = mainHand;
    }

    /**
     * Gets the new main hand of the player. The old hand is still momentarily
     * available via {@link Player#getMainHand()}.
     *
     * @return the new {@link MainHand} of the player
     */
    public MainHand getMainHand() {
        return mainHand;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
