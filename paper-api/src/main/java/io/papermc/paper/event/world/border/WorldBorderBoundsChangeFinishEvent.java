package io.papermc.paper.event.world.border;

import org.bukkit.event.HandlerList;

/**
 * Called when a moving world border has finished its move.
 */
public interface WorldBorderBoundsChangeFinishEvent extends WorldBorderEvent {

    /**
     * Gets the old size of the worldborder.
     *
     * @return the old size
     */
    double getOldSize();

    /**
     * Gets the new size of the worldborder.
     *
     * @return the new size
     */
    double getNewSize();

    /**
     * Gets the duration this worldborder took to make the change.
     * <p>
     * Can be 0 if handlers for {@link WorldBorderCenterChangeEvent} set the duration to 0.
     *
     * @return the duration of the transition
     */
    double getDuration();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
