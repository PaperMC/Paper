package io.papermc.paper.event.world.border;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a world border's center is changed.
 */
@NullMarked
public class WorldBorderCenterChangeEvent extends WorldBorderEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location oldCenter;
    private Location newCenter;

    private boolean cancelled;

    @ApiStatus.Internal
    public WorldBorderCenterChangeEvent(final World world, final WorldBorder worldBorder, final Location oldCenter, final Location newCenter) {
        super(world, worldBorder);
        this.oldCenter = oldCenter;
        this.newCenter = newCenter;
    }

    /**
     * Gets the original center location of the world border.
     *
     * @return the old center
     */
    public Location getOldCenter() {
        return this.oldCenter.clone();
    }

    /**
     * Gets the new center location for the world border.
     *
     * @return the new center
     */
    public Location getNewCenter() {
        return this.newCenter;
    }

    /**
     * Sets the new center location for the world border. Y coordinate is ignored.
     *
     * @param newCenter the new center
     */
    public void setNewCenter(final Location newCenter) {
        this.newCenter = newCenter.clone();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
