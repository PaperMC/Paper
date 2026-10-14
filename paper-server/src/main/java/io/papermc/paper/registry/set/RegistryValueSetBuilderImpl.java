package io.papermc.paper.registry.set;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Keyed;

public class RegistryValueSetBuilderImpl<M, API extends Keyed, BUILDER extends RegistryBuilder<API>> implements RegistryValueSetBuilder<API, BUILDER> { // TODO remove Keyed

    private final RegistryKey<API> registryKey;
    private final Conversions conversions;
    private final List<API> instances = new ArrayList<>();

    public RegistryValueSetBuilderImpl(final RegistryKey<API> registryKey, final Conversions conversions) {
        this.registryKey = registryKey;
        this.conversions = conversions;
    }

    @Override
    public RegistryValueSetBuilder<API, BUILDER> add(final Consumer<RegistryBuilderFactory<API, ? extends BUILDER>> builder) {
        this.instances.add(this.conversions.createApiInstanceFromBuilder(this.registryKey, builder));
        return this;
    }

    @Override
    public RegistryValueSet<API> build() {
        return RegistrySet.valueSet(this.registryKey, this.instances);
    }
}
