package org.bukkit.support.environment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.bukkit.support.extension.AllFeaturesExtension;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Indicates that a class or method should be tested with all Minecraft feature flags set.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("AllFeatures")
@ExtendWith({AllFeaturesExtension.class})
public @interface AllFeatures {
}
