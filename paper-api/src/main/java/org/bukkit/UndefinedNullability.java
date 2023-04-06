package org.bukkit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.jetbrains.annotations.ApiStatus;

/**
 * Annotation for types, whose nullability is not well defined, so
 * {@link org.jetbrains.annotations.NotNull} nor
 * {@link org.jetbrains.annotations.Nullable} is applicable. For example when
 * interface defines a method, whose nullability depends on the implementation.
 *
 * @deprecated This should generally not be used in any new API code as it
 * suggests a bad API design.
 */
@Retention(RetentionPolicy.CLASS)
@ApiStatus.Internal
@Deprecated
public @interface UndefinedNullability {

    /**
     * Human readable description of the circumstances, in which the type is
     * nullable.
     *
     * @return description
     */
    String value() default "";
}
