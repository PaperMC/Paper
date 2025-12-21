package io.papermc.paper.registry.data;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.function.Consumer;
import org.bukkit.MusicInstrument;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

public final class InlinedRegistryBuilderProviderImpl implements InlinedRegistryBuilderProvider {

    @Override
    public MusicInstrument createInstrument(final Consumer<RegistryBuilderFactory<MusicInstrument, ? extends InstrumentRegistryEntry.Builder>> value) {
        return Conversions.global().createApiInstanceFromBuilder(RegistryKey.INSTRUMENT, value);
    }

    @Override
    public Dialog createDialog(final Consumer<RegistryBuilderFactory<Dialog, ? extends DialogRegistryEntry.Builder>> value) {
        return Conversions.global().createApiInstanceFromBuilder(RegistryKey.DIALOG, value);
    }

    @Override
    public TrimMaterial createTrimMaterial(final Consumer<RegistryBuilderFactory<TrimMaterial, ? extends TrimMaterialRegistryEntry.Builder>> value) {
        return Conversions.global().createApiInstanceFromBuilder(RegistryKey.TRIM_MATERIAL, value);
    }

    @Override
    public TrimPattern createTrimPattern(final Consumer<RegistryBuilderFactory<TrimPattern, ? extends TrimPatternRegistryEntry.Builder>> value) {
        return Conversions.global().createApiInstanceFromBuilder(RegistryKey.TRIM_PATTERN, value);
    }
}
