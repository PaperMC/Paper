package org.bukkit.support.environment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.bukkit.support.extension.VanillaFeatureExtension;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Indicates that a class or method should be tested with Minecraft's vanilla feature flag set.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("VanillaFeature")
@ExtendWith({VanillaFeatureExtension.class})
public @interface VanillaFeature {
}
