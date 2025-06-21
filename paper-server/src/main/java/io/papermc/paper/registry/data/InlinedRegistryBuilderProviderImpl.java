package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import org.bukkit.MusicInstrument;

public final class InlinedRegistryBuilderProviderImpl implements InlinedRegistryBuilderProvider {

    @Override
    public MusicInstrument createInstrument(final Consumer<RegistryBuilderFactory<MusicInstrument, ? extends InstrumentRegistryEntry.Builder>> value) {
        return Conversions.global().createApiInstanceFromBuilder(Registries.INSTRUMENT, value);
    }
}
