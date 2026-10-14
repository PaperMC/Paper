package io.papermc.paper.registry.set;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import java.util.function.Consumer;
import org.jetbrains.annotations.ApiStatus;

/**
 * A builder for a {@link RegistryValueSet}.
 *
 * @param <API> the API type
 * @param <ENTRY_BUILDER> the type of the entry builder,
 */
@ApiStatus.NonExtendable
public interface RegistryValueSetBuilder<API, ENTRY_BUILDER extends RegistryBuilder<API>> {

    /**
     * Adds a value to the registry value set.
     *
     * @param builder the builder for the value to add
     * @return this builder for chaining
     */
    RegistryValueSetBuilder<API, ENTRY_BUILDER> add(Consumer<RegistryBuilderFactory<API, ? extends ENTRY_BUILDER>> builder);

    /**
     * Builds the {@link RegistryValueSet}.
     *
     * @return the built registry value set
     */
    RegistryValueSet<API> build();
}
