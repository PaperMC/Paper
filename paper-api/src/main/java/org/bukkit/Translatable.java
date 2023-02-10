package org.bukkit;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an object with a text representation that can be translated by the
 * Minecraft client.
 */
public interface Translatable {

    /**
     * Get the translation key, suitable for use in a translation component.
     *
     * @return the translation key
     */
    @NotNull
    String getTranslationKey();
}
