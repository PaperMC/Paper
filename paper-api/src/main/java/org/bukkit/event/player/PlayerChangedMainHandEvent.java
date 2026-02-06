package org.bukkit.event.player;

import com.destroystokyo.paper.event.player.PlayerClientOptionsChangeEvent;
import org.bukkit.entity.Player;
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
     * Gets the new main hand of the player. The old hand is still momentarily
     * available via {@link Player#getMainHand()}.
     *
     * @return the new {@link MainHand} of the player
     * @deprecated has never been functional since its implementation and simply returns the old main hand.
     * The method is left in this broken state to not break compatibility with plugins that relied on this fact.
     * Use {@link #getNewMainHand()} instead or migrate to {@link PlayerClientOptionsChangeEvent#getMainHand()}.
     */
    @Deprecated(since = "1.21.4", forRemoval = true)
    MainHand getMainHand();

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
