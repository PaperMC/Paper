package org.bukkit.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark methods as being event handler methods
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

    /**
     * Define the priority of the event.
     * <p>
     * First priority to the last priority executed:
     * <ol>
     * <li>LOWEST
     * <li>LOW
     * <li>NORMAL
     * <li>HIGH
     * <li>HIGHEST
     * <li>MONITOR
     * </ol>
     *
     * @return the priority
     */
    EventPriority priority() default EventPriority.NORMAL;

    /**
     * Define if the handler ignores a cancelled event.
     * <p>
     * If ignoreCancelled is true and the event is cancelled, the method is
     * not called. Otherwise, the method is always called.
     *
     * @return whether cancelled events should be ignored
     */
    boolean ignoreCancelled() default false;
}
