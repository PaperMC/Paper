package org.bukkit.event;

import org.bukkit.plugin.PluginManager;

/**
 * Represents an event.
 *
 * All events require a static method named getHandlerList() which returns the same {@link HandlerList} as {@link #getHandlers()}.
 *
 * @see PluginManager#callEvent(Event)
 * @see PluginManager#registerEvents(Listener,Plugin)
 */
public abstract class Event {
    private String name;
    private final boolean async;

    /**
     * The default constructor is defined for cleaner code. This constructor
     * assumes the event is synchronous.
     */
    public Event() {
        this(false);
    }

    /**
     * This constructor is used to explicitly declare an event as synchronous
     * or asynchronous.
     *
     * @param isAsync true indicates the event will fire asynchronously, false
     *     by default from default constructor
     */
    public Event(boolean isAsync) {
        this.async = isAsync;
    }

    /**
     * Convenience method for providing a user-friendly identifier. By
     * default, it is the event's class's {@linkplain Class#getSimpleName()
     * simple name}.
     *
     * @return name of this event
     */
    public String getEventName() {
        if (name == null) {
            name = getClass().getSimpleName();
        }
        return name;
    }

    public abstract HandlerList getHandlers();

    /**
     * Any custom event that should not by synchronized with other events must
     * use the specific constructor. These are the caveats of using an
     * asynchronous event:
     * <ul>
     * <li>The event is never fired from inside code triggered by a
     *     synchronous event. Attempting to do so results in an {@link
     *     java.lang.IllegalStateException}.
     * <li>However, asynchronous event handlers may fire synchronous or
     *     asynchronous events
     * <li>The event may be fired multiple times simultaneously and in any
     *     order.
     * <li>Any newly registered or unregistered handler is ignored after an
     *     event starts execution.
     * <li>The handlers for this event may block for any length of time.
     * <li>Some implementations may selectively declare a specific event use
     *     as asynchronous. This behavior should be clearly defined.
     * <li>Asynchronous calls are not calculated in the plugin timing system.
     * </ul>
     *
     * @return false by default, true if the event fires asynchronously
     */
    public final boolean isAsynchronous() {
        return async;
    }

    public enum Result {

        /**
         * Deny the event. Depending on the event, the action indicated by the
         * event will either not take place or will be reverted. Some actions
         * may not be denied.
         */
        DENY,
        /**
         * Neither deny nor allow the event. The server will proceed with its
         * normal handling.
         */
        DEFAULT,
        /**
         * Allow / Force the event. The action indicated by the event will
         * take place if possible, even if the server would not normally allow
         * the action. Some actions may not be allowed.
         */
        ALLOW;
    }
}
