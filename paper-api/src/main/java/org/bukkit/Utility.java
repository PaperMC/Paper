package org.bukkit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates a method (and sometimes constructor) will chain
 * its internal operations.
 * <p>
 * This is solely meant for identifying methods that don't need to be
 * overridden / handled manually.
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface Utility {
}
