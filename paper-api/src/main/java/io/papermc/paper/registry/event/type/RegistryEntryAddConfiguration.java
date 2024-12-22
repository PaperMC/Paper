package io.papermc.paper.registry.event.type;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.PrioritizedLifecycleEventHandlerConfiguration;
import io.papermc.paper.registry.TypedKey;
import java.util.function.Predicate;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Specific configuration for {@link io.papermc.paper.registry.event.RegistryEntryAddEvent}s.
 *
 * @param <T> registry entry type
 * @since 1.21
 */
@NullMarked
public interface RegistryEntryAddConfiguration<T> extends PrioritizedLifecycleEventHandlerConfiguration<BootstrapContext> {

    /**
     * Only call the handler if the value being added matches the specified key.
     *
     * @param key the key to match
     * @return this configuration
     */
    @Contract(value = "_ -> this", mutates = "this")
    default RegistryEntryAddConfiguration<T> filter(final TypedKey<T> key) {
        return this.filter(key::equals);
    }

    /**
     * Only call the handler if the value being added passes the provided filter.
     *
     * @param filter the predicate to match the key against
     * @return this configuration
     */
    @Contract(value = "_ -> this", mutates = "this")
    RegistryEntryAddConfiguration<T> filter(Predicate<TypedKey<T>> filter);

    @Override
    RegistryEntryAddConfiguration<T> priority(int priority);

    @Override
    RegistryEntryAddConfiguration<T> monitor();
}
