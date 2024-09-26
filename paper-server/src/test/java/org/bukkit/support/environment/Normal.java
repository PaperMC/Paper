package org.bukkit.support.environment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.bukkit.support.extension.NormalExtension;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Indicates that a class or method is an independent test and does not require direct access to registry values.
 * To allow loading of classes with registry values,
 * they are populated with mocks that will throw an error if accessed.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("Normal")
@ExtendWith({NormalExtension.class})
public @interface Normal {
}
