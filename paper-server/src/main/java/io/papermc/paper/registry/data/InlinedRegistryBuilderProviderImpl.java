package io.papermc.paper.registry.data;

import io.papermc.paper.block.TrialSpawnerConfig;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;

public final class InlinedRegistryBuilderProviderImpl implements InlinedRegistryBuilderProvider {

    @Override
    public TrialSpawnerConfig createTrialSpawnerConfig(final Consumer<RegistryBuilderFactory<TrialSpawnerConfig, ? extends TrialSpawnerConfigRegistryEntry.Builder>> value) {
        return Conversions.global().createApiInstanceFromBuilder(Registries.TRIAL_SPAWNER_CONFIG, value);
    }
}
