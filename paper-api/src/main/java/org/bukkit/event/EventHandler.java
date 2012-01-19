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
     * This field is now fetched from the event handler method's parameter
     * @return
     */
    @Deprecated Class<? extends Event> event() default Event.class;

    EventPriority priority() default EventPriority.NORMAL;
}
