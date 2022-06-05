package org.bukkit.advancement;

import java.util.Collection;
import org.bukkit.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an advancement that may be awarded to a player. This class is not
 * reference safe as the underlying advancement may be reloaded.
 */
public interface Advancement extends Keyed {

    /**
     * Get all the criteria present in this advancement.
     *
     * @return a unmodifiable copy of all criteria
     */
    @NotNull
    Collection<String> getCriteria();

    /**
     * Returns the display information for this advancement.
     *
     * This includes it's name, description and other visible tags.
     *
     * @return a AdvancementDisplay object, or null if not set.
     */
    @Nullable
    AdvancementDisplay getDisplay();
}
