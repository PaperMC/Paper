package io.papermc.paper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.ApiStatus;

/// Annotation used to mark methods or constructors which should not be called.
///
/// Separate from [Deprecated] to differentiate from the large amount of deprecations.
@ApiStatus.Internal
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface DoNotUse {
}
