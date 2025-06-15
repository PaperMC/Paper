package io.papermc.paper.registry.set;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import java.util.function.Consumer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface RegistryValueSetBuilder<API, ENTRY_BUILDER extends RegistryBuilder<API>> {

    /**
     *
     * @param builder
     * @return
     */
    RegistryValueSetBuilder<API, ENTRY_BUILDER> add(Consumer<RegistryBuilderFactory<API, ? extends ENTRY_BUILDER>> builder);

    RegistryValueSet<API> build();
}
