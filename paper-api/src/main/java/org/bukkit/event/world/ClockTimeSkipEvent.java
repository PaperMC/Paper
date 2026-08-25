package org.bukkit.event.world;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;

/**
 * Called when the time skips for a world clock.
 * <p>
 * If the event is cancelled the time will not change.
 */
@ApiStatus.Experimental
public interface ClockTimeSkipEvent extends Event, Cancellable {

    /**
     * Gets the reason why the time has skipped.
     *
     * @return a SkipReason value detailing why the time has skipped
     */
    SkipReason getSkipReason();

    /**
     * Gets the amount of time that was skipped.
     *
     * @return Amount of time skipped
     */
    long getSkipAmount();

    /**
     * Sets the amount of time to skip.
     *
     * @param skipAmount Amount of time to skip
     */
    void setSkipAmount(long skipAmount);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * An enum specifying the reason the time skipped.
     */
    enum SkipReason {

        /**
         * When time is changed using the vanilla /time command.
         */
        COMMAND,
        /**
         * When time is changed by a plugin.
         */
        CUSTOM,
        /**
         * When time is changed by all players sleeping in their beds and the
         * night skips.
         */
        NIGHT_SKIP
    }
}
