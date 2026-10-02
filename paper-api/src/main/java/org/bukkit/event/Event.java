package org.bukkit.event;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

/// Represents an event.
///
/// All events require a static method named getHandlerList() which returns the same [HandlerList] as [#getHandlers()].
///
/// @see PluginManager#callEvent(Event)
/// @see PluginManager#registerEvents(Listener,Plugin)
public abstract class Event {

    private final boolean isAsync;

    /// The default constructor is defined for cleaner code. This constructor
    /// assumes the event is synchronous.
    public Event() {
        this(false);
    }

    /// This constructor is used to explicitly declare an event as synchronous
    /// or asynchronous.
    ///
    /// @param isAsync`true` indicates the event will fire asynchronously, `false`
    ///     by default from default constructor
    public Event(boolean isAsync) {
        this.isAsync = isAsync;
    }

    /// Calls the event and tests if cancelled.
    ///
    /// @return `false` if event was cancelled, if cancellable. otherwise `true`.
    public boolean callEvent() {
        Bukkit.getPluginManager().callEvent(this);
        if (this instanceof Cancellable) {
            return !((Cancellable) this).isCancelled();
        } else {
            return true;
        }
    }

    /// Convenience method for providing a user-friendly identifier. By
    /// default, it is the event's class's [simple name][Class#getSimpleName()].
    ///
    /// @return name of this event
    @NotNull
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

    @NotNull
    public abstract HandlerList getHandlers();

    /// Any custom event that should not be synchronized with other events must
    /// use the specific constructor. These are the caveats of using an
    /// asynchronous event:
    ///
    ///   - The event is never fired from inside code triggered by a
    ///     synchronous event. Attempting to do so results in an
    ///     [IllegalStateException].
    ///   - However, asynchronous event handlers may fire synchronous or
    ///     asynchronous events
    ///   - The event may be fired multiple times simultaneously and in any
    ///     order.
    ///   - Any newly registered or unregistered handler is ignored after an
    ///     event starts execution.
    ///   - The handlers for this event may block for any length of time.
    ///   - Some implementations may selectively declare a specific event use
    ///     as asynchronous. This behavior should be clearly defined.
    ///
    /// @return `false` by default, `true` if the event fires asynchronously
    public final boolean isAsynchronous() {
        return this.isAsync;
    }

    public enum Result {

        /// Deny the event. Depending on the event, the action indicated by the
        /// event will either not take place or will be reverted. Some actions
        /// may not be denied.
        DENY,
        /// Neither deny nor allow the event. The server will proceed with its
        /// normal handling.
        DEFAULT,
        /// Allow / Force the event. The action indicated by the event will
        /// take place if possible, even if the server would not normally allow
        /// the action. Some actions may not be allowed.
        ALLOW
    }
}
