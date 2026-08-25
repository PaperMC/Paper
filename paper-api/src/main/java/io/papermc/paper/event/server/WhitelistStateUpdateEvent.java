package io.papermc.paper.event.server;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event gets called when the whitelist status of a player is changed
 */
public interface WhitelistStateUpdateEvent extends Event, Cancellable {

    /**
     * Gets the player whose whitelist status is being changed
     *
     * @return the player whose status is being changed
     */
    OfflinePlayer getPlayer();

    /**
     * Gets the player profile whose whitelist status is being changed
     *
     * @return the player profile whose status is being changed
     */
    PlayerProfile getPlayerProfile();

    /**
     * Gets the status change of the player profile
     *
     * @return the whitelist status
     */
    WhitelistStatus getStatus();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * Enum for the whitelist status changes
     */
    enum WhitelistStatus {
        ADDED, REMOVED
    }
}
