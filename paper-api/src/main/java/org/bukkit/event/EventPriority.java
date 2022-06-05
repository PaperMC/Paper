package org.bukkit.event;

/**
 * Represents an event's priority in execution.
 * <p>
 * Listeners with lower priority are called first
 * will listeners with higher priority are called last.
 * <p>
 * Listeners are called in following order:
 * {@link #LOWEST} -> {@link #LOW} -> {@link #NORMAL} -> {@link #HIGH} -> {@link #HIGHEST} -> {@link #MONITOR}
 */
public enum EventPriority {

    /**
     * Event call is of very low importance and should be run first, to allow
     * other plugins to further customise the outcome
     */
    LOWEST(0),
    /**
     * Event call is of low importance
     */
    LOW(1),
    /**
     * Event call is neither important nor unimportant, and may be run
     * normally
     */
    NORMAL(2),
    /**
     * Event call is of high importance
     */
    HIGH(3),
    /**
     * Event call is critical and must have the final say in what happens
     * to the event
     */
    HIGHEST(4),
    /**
     * Event is listened to purely for monitoring the outcome of an event.
     * <p>
     * No modifications to the event should be made under this priority
     */
    MONITOR(5);

    private final int slot;

    private EventPriority(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }
}
