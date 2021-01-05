package io.papermc.paper.event.world.border;

import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a moving world border has finished its move.
 */
@NullMarked
public class WorldBorderBoundsChangeFinishEvent extends WorldBorderEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final double oldSize;
    private final double newSize;
    private final double duration;

    @ApiStatus.Internal
    public WorldBorderBoundsChangeFinishEvent(final World world, final WorldBorder worldBorder, final double oldSize, final double newSize, final double duration) {
        super(world, worldBorder);
        this.oldSize = oldSize;
        this.newSize = newSize;
        this.duration = duration;
    }

    /**
     * Gets the old size of the worldborder.
     *
     * @return the old size
     */
    public double getOldSize() {
        return this.oldSize;
    }

    /**
     * Gets the new size of the worldborder.
     *
     * @return the new size
     */
    public double getNewSize() {
        return this.newSize;
    }

    /**
     * Gets the duration this worldborder took to make the change.
     * <p>
     * Can be 0 if handlers for {@link WorldBorderCenterChangeEvent} set the duration to 0.
     *
     * @return the duration of the transition
     */
    public double getDuration() {
        return this.duration;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
