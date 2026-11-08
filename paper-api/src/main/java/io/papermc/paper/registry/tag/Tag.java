package io.papermc.paper.registry.tag;

import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

/**
 * A named {@link RegistryKeySet} which are created
 * via the datapack tag system.
 *
 * @param <T> the type of the values in the tag
 * @see org.bukkit.Tag
 * @see org.bukkit.Registry#getTag(TagKey)
 */
@ApiStatus.Experimental
public interface Tag<T extends Keyed> extends RegistryKeySet<T> { // TODO remove Keyed

    /**
     * Get the identifier for this named set.
     *
     * @return the tag key identifier
     */
    TagKey<T> tagKey();
}
