package io.papermc.paper.event.world.border;

import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a world border changes its bounds, either over time, or instantly.
 */
@NullMarked
public class WorldBorderBoundsChangeEvent extends WorldBorderEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Type type;
    private final double oldSize;
    private double newSize;
    private long duration;
    private boolean cancelled;

    @ApiStatus.Internal
    public WorldBorderBoundsChangeEvent(final World world, final WorldBorder worldBorder, final Type type, final double oldSize, final double newSize, final long duration) {
        super(world, worldBorder);
        this.type = type;
        this.oldSize = oldSize;
        this.newSize = newSize;
        this.duration = duration;
    }

    /**
     * Gets if this change is an instant change or over-time change.
     *
     * @return the change type
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Gets the old size or the world border.
     *
     * @return the old size
     */
    public double getOldSize() {
        return this.oldSize;
    }

    /**
     * Gets the new size of the world border.
     *
     * @return the new size
     */
    public double getNewSize() {
        return this.newSize;
    }

    /**
     * Sets the new size of the world border.
     *
     * @param newSize the new size
     */
    public void setNewSize(final double newSize) {
        this.newSize = Math.min(this.worldBorder.getMaxSize(), Math.max(1.0D, newSize));
    }

    /**
     * Gets the time in milliseconds for the change. Will be 0 if instant.
     *
     * @return the time in milliseconds for the change
     */
    public long getDuration() {
        return this.duration;
    }

    /**
     * Sets the time in milliseconds for the change. Will change {@link #getType()} to return
     * {@link Type#STARTED_MOVE}.
     *
     * @param duration the time in milliseconds for the change
     */
    public void setDuration(final long duration) {
        // PAIL: TODO: Magic Values
        this.duration = Math.min(9223372036854775L, Math.max(0L, duration));
        if (duration >= 0 && this.type == Type.INSTANT_MOVE) {
            this.type = Type.STARTED_MOVE;
        }
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

    public enum Type {
        STARTED_MOVE,
        INSTANT_MOVE
    }
}
