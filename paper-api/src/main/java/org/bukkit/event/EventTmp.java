package org.bukkit.event;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event.
 * <br>
 * All events require a static method named getHandlerList() which returns the same {@link HandlerList} as {@link #getHandlers()}.
 *
 * @see PluginManager#callEvent(Event)
 * @see PluginManager#registerEvents(Listener,Plugin)
 */
@Deprecated(forRemoval = true)
public abstract class EventTmp implements Event {

    private String name;
    private final boolean isAsync;

    /**
     * The default constructor is defined for cleaner code. This constructor
     * assumes the event is synchronous.
     */
    public EventTmp() {
        this(false);
    }

    /**
     * This constructor is used to explicitly declare an event as synchronous
     * or asynchronous.
     *
     * @param isAsync {@code true} indicates the event will fire asynchronously, {@code false}
     *     by default from default constructor
     */
    public EventTmp(boolean isAsync) {
        this.isAsync = isAsync;
    }

    /**
     * Calls the event and tests if cancelled.
     *
     * @return {@code false} if event was cancelled, if cancellable. otherwise {@code true}.
     */
    public boolean callEvent() {
        Bukkit.getPluginManager().callEvent(this);
        if (this instanceof Cancellable) {
            return !((Cancellable) this).isCancelled();
        } else {
            return true;
        }
    }

    /**
     * Convenience method for providing a user-friendly identifier. By
     * default, it is the event's class's {@linkplain Class#getSimpleName()
     * simple name}.
     *
     * @return name of this event
     */
    @NotNull
    public String getEventName() {
        if (this.name == null) {
            this.name = this.getClass().getSimpleName();
        }
        return this.name;
    }

    @NotNull
    public abstract HandlerList getHandlers();

    /**
     * Any custom event that should not be synchronized with other events must
     * use the specific constructor. These are the caveats of using an
     * asynchronous event:
     * <ul>
     * <li>The event is never fired from inside code triggered by a
     *     synchronous event. Attempting to do so results in an {@link
     *     IllegalStateException}.
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
     * @return {@code false} by default, {@code true} if the event fires asynchronously
     */
    public final boolean isAsynchronous() {
        return this.isAsync;
    }
}
