package io.papermc.paper.registry.data;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.function.Consumer;
import org.bukkit.MusicInstrument;

public final class InlinedRegistryBuilderProviderImpl implements InlinedRegistryBuilderProvider {

    @Override
    public MusicInstrument createInstrument(final Consumer<RegistryBuilderFactory<MusicInstrument, ? extends InstrumentRegistryEntry.Builder>> value) {
        return Conversions.global().createApiInstanceFromBuilder(RegistryKey.INSTRUMENT, value);
    }

    @Override
    public Dialog createDialog(final Consumer<RegistryBuilderFactory<Dialog, ? extends DialogRegistryEntry.Builder>> value) {
        return Conversions.global().createApiInstanceFromBuilder(RegistryKey.DIALOG, value);
    }
}
