package io.papermc.paper.registry.set;

import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.holder.RegistryHolder;
import java.util.Collection;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A {@link RegistrySet} backed by {@link RegistryHolder holders}, each of which is
 * either a reference to a registry value or an inlined, anonymous value.
 * <p>
 *     Create with {@link RegistrySet#holderSetBuilder(RegistryKey)}, or if in a registry event context,
 *     {@link io.papermc.paper.registry.event.RegistryFactory#createSetBuilder(RegistryKey)}.
 * </p>
 *
 * @param <T> the API type
 * @param <E> the registry entry type
 */
public non-sealed interface RegistryHolderSet<T extends RegistryElement.Buildable<T, E, ?>, E> extends RegistrySet<T> {

    /**
     * Gets the holders in this set.
     *
     * @return an unmodifiable collection of holders
     */
    @Unmodifiable Collection<RegistryHolder<T, E>> holders();
}
