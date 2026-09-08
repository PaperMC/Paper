package org.bukkit.event.player;

import com.destroystokyo.paper.event.player.PlayerClientOptionsChangeEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MainHand;
import org.jetbrains.annotations.ApiStatus;

/**
 * Called when a player changes their main hand in the client settings.
 *
 * @apiNote Obsolete and replaced by {@link PlayerClientOptionsChangeEvent}.
 */
@ApiStatus.Obsolete
public interface PlayerChangedMainHandEvent extends PlayerEvent {

    /**
     * Gets the new main hand of the player.
     *
     * @return the new {@link MainHand} of the player
     */
    MainHand getNewMainHand();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
