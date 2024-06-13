package org.bukkit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.ApiStatus;

/**
 * Indicates that the annotated element (class, method, field, etc.) is part of a
 * <a href="https://minecraft.wiki/w/Experimental_Gameplay">minecraft experimental feature</a>
 * and is subject to changes by Mojang.
 * <p>
 * <b>Note:</b> Elements marked with this annotation require the use of a datapack or otherwise
 * non-standard feature to be enabled on the server.
 *
 * @see <a href="https://www.minecraft.net/en-us/article/testing-new-minecraft-features/feature-toggles-java-edition">Features Toggles - Minecraft Article</a>
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE
})
@ApiStatus.Internal
public @interface MinecraftExperimental {

    /**
     * Get the feature that must be enabled for the annotated object to be valid.
     * <p>
     * While this value is not used anywhere in Bukkit, it is a convenience value to assist
     * in locating relevant annotated elements for removal once no longer deemed an experimental
     * feature by Minecraft. See {@link Requires} for information about use in plugins.
     *
     * @return the required feature flag
     */
    Requires value();

    /**
     * An enum identifying a feature flag required by a {@link MinecraftExperimental} feature.
     * <p>
     * Constants defined by this enum <strong>ARE NOT API!</strong> Constants may be added or
     * removed without warning and will not necessarily align perfectly with those defined in
     * FeatureFlag. At no point should plugins depend on this enum. Refer to {@link FeatureFlag}
     * instead.
     */
    @ApiStatus.Internal
    public enum Requires {

    }
}
