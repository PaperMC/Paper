package org.bukkit.support.environment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.bukkit.support.extension.SlowExtension;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Indicates that a class or method is a slow test, which takes a longer time to complete.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("Slow")
@ExtendWith({SlowExtension.class})
public @interface Slow {

    /**
     * The reason / estimated time to complete, this is purely for documentation.
     */
    String value() default "";
}
