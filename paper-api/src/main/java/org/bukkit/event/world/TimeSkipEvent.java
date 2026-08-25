package org.bukkit.event.world;

/**
 * Called when the time skips in a world.
 * <p>
 * If the event is cancelled the time will not change.
 *
 * @see ClockTimeSkipEvent for changing of clocks that affect all worlds
 */
public interface TimeSkipEvent extends ClockTimeSkipEvent, WorldEvent {
}
