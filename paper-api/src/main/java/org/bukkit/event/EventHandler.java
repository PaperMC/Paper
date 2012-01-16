package org.bukkit.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation to mark methods as being event handler methods
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

    Class<? extends Event> event();

    EventPriority priority();
}
