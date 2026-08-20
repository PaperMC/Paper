package org.bukkit.event.player;

import io.papermc.paper.event.player.AbstractRespawnEvent;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

/**
 * Called when a player respawns.
 * <p>
 * If changing player state, see {@link com.destroystokyo.paper.event.player.PlayerPostRespawnEvent}
 * because the player is "reset" between this event and that event and some changes won't persist.
 */
public interface PlayerRespawnEvent extends AbstractRespawnEvent {

    /**
     * Sets the new respawn location.
     *
     * @param respawnLocation new location for the respawn
     */
    void setRespawnLocation(Location respawnLocation);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * An enum to specify the reason a respawn event was called.
     */
    enum RespawnReason {

        /**
         * When the player dies and presses the respawn button.
         */
        DEATH,
        /**
         * When the player exits the end through the end portal.
         */
        END_PORTAL,
        /**
         * When a plugin respawns the player.
         */
        PLUGIN
    }

    @Deprecated
    enum RespawnFlag {
        /**
         * Will use the bed spawn location
         */
        BED_SPAWN,
        /**
         * Will use the respawn anchor location
         */
        ANCHOR_SPAWN,
        /**
         * Is caused by going to the end portal in the end.
         */
        END_PORTAL
    }
}
