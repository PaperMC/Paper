package org.bukkit.event.raid;

import org.bukkit.Raid;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Called when a {@link Raid} is triggered (e.g: a player with Bad Omen effect
 * enters a village).
 */
public interface RaidTriggerEvent extends RaidEvent, PlayerEvent, Cancellable {

    /**
     * Returns the player who triggered the raid.
     *
     * @return triggering player
     */
    @Override
    Player getPlayer();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
