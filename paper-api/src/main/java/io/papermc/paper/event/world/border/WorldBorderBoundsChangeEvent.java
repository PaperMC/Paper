package io.papermc.paper.event.world.border;

import io.papermc.paper.util.Tick;
import java.time.Duration;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.index.qual.NonNegative;

/**
 * Called when a world border changes its bounds, either over time, or instantly.
 */
public interface WorldBorderBoundsChangeEvent extends WorldBorderEvent, Cancellable {

    /**
     * Gets if this change is an instant change or over-time change.
     *
     * @return the change type
     */
    Type getType();

    /**
     * Gets the old size or the world border.
     *
     * @return the old size
     */
    double getOldSize();

    /**
     * Gets the new size of the world border.
     *
     * @return the new size
     */
    double getNewSize();

    /**
     * Sets the new size of the world border.
     *
     * @param newSize the new size
     */
    void setNewSize(double newSize);

    /**
     * Gets the time in ticks for the change. Will be 0 if instant.
     *
     * @return the time in ticks for the change
     */
    @NonNegative long getDurationTicks();

    /**
     * Sets the time in ticks for the change. Will change {@link #getType()} to return
     * {@link Type#STARTED_MOVE}.
     *
     * @param duration the time in ticks for the change
     */
    void setDurationTicks(@NonNegative long duration);

    /**
     * Gets the time in milliseconds for the change. Will be 0 if instant.
     *
     * @return the time in milliseconds for the change
     * @deprecated in favor of {@link #getDurationTicks()}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    default @NonNegative long getDuration() {
        return Tick.of(this.getDurationTicks()).toMillis();
    }

    /**
     * Sets the time in milliseconds for the change. Will change {@link #getType()} to return
     * {@link Type#STARTED_MOVE}.
     *
     * @param duration the time in milliseconds for the change
     * @deprecated in favor of {@link #setDurationTicks(long)}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    default void setDuration(final @NonNegative long duration) {
        this.setDurationTicks(Tick.tick().fromDuration(Duration.ofMillis(duration)));
    }

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum Type {
        STARTED_MOVE,
        INSTANT_MOVE
    }
}
