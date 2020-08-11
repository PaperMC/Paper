package org.bukkit;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an object with a text representation that can be translated by the
 * Minecraft client.
 * @deprecated use {@link net.kyori.adventure.translation.Translatable}
 */
@Deprecated(forRemoval = true) // Paper
public interface Translatable {

    /**
     * Get the translation key, suitable for use in a translation component.
     *
     * @return the translation key
     * @deprecated look for a {@code translationKey()} method instead
     */
    @NotNull
    @Deprecated(forRemoval = true) // Paper
    String getTranslationKey();
}
