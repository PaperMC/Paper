package org.bukkit.event.raid;

import org.bukkit.Raid;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a {@link Raid} is stopped.
 */
public class RaidStopEvent extends RaidEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Reason reason;

    @ApiStatus.Internal
    public RaidStopEvent(@NotNull Raid raid, @NotNull World world, @NotNull Reason reason) {
        super(raid, world);
        this.reason = reason;
    }

    /**
     * Returns the stop reason.
     *
     * @return Reason
     */
    @NotNull
    public Reason getReason() {
        return this.reason;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum Reason {

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
