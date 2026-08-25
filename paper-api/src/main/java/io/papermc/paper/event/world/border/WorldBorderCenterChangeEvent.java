package io.papermc.paper.event.world.border;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a world border's center is changed.
 */
public interface WorldBorderCenterChangeEvent extends WorldBorderEvent, Cancellable {

    /**
     * Gets the original center location of the world border.
     *
     * @return the old center
     */
    Location getOldCenter();

    /**
     * Gets the new center location for the world border.
     *
     * @return the new center
     */
    Location getNewCenter();

    /**
     * Sets the new center location for the world border. Y coordinate is ignored.
     *
     * @param newCenter the new center
     */
    void setNewCenter(Location newCenter);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
