package org.bukkit.event.raid;

import org.bukkit.Raid;
import org.bukkit.event.HandlerList;

/**
 * Called when a {@link Raid} is stopped.
 */
public interface RaidStopEvent extends RaidEvent {

    /**
     * Returns the stop reason.
     *
     * @return Reason
     */
    Reason getReason();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum Reason {

        /**
         * Because the difficulty has been changed to peaceful.
         */
        PEACE,
        /**
         * The raid took a long time without a final result.
         */
        TIMEOUT,
        /**
         * Finished the raid.
         */
        FINISHED,
        /**
         * Couldn't find a suitable place to spawn raiders.
         */
        UNSPAWNABLE,
        /**
         * The place where the raid occurs no longer be a village.
         */
        NOT_IN_VILLAGE
    }
}
