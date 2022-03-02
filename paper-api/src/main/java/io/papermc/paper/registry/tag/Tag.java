package io.papermc.paper.registry.tag;

import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A named {@link RegistryKeySet} which are created
 * via the datapack tag system.
 *
 * @param <T>
 * @see org.bukkit.Tag
 * @see org.bukkit.Registry#getTag(TagKey)
 */
@ApiStatus.Experimental
@NullMarked
public interface Tag<T extends Keyed> extends RegistryKeySet<T> { // TODO remove Keyed

    /**
     * Get the identifier for this named set.
     *
     * @return the tag key identifier
     */
    TagKey<T> tagKey();
}
