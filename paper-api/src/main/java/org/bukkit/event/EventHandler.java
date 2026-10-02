package org.bukkit.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// An annotation to mark methods as being event handler methods
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

    /// Define the priority of the event.
    ///
    /// First priority to the last priority executed:
    ///
    ///   1. LOWEST
    ///   2. LOW
    ///   3. NORMAL
    ///   4. HIGH
    ///   5. HIGHEST
    ///   6. MONITOR
    ///
    /// @return the priority
    EventPriority priority() default EventPriority.NORMAL;

    /// Define if the handler ignores a cancelled event.
    ///
    /// If ignoreCancelled is `true` and the event is cancelled, the method is
    /// not called. Otherwise, the method is always called.
    ///
    /// @return whether cancelled events should be ignored
    boolean ignoreCancelled() default false;
}
