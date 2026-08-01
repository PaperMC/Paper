package io.papermc.paper.registry.data;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import org.bukkit.MusicInstrument;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.ApiStatus;

/**
 * @hidden
 */
@ApiStatus.Internal
@ApiStatus.NonExtendable
public interface InlinedRegistryBuilderProvider {

    static InlinedRegistryBuilderProvider instance() {
        final class Holder {
            static final Optional<InlinedRegistryBuilderProvider> INSTANCE = ServiceLoader.load(InlinedRegistryBuilderProvider.class, InlinedRegistryBuilderProvider.class.getClassLoader()).findFirst();
        }
        return Holder.INSTANCE.orElseThrow();
    }

    MusicInstrument createInstrument(Consumer<RegistryBuilderFactory<MusicInstrument, ? extends InstrumentRegistryEntry.Builder>> value);

    Dialog createDialog(Consumer<RegistryBuilderFactory<Dialog, ? extends DialogRegistryEntry.Builder>> value);

    TrimMaterial createTrimMaterial(Consumer<RegistryBuilderFactory<TrimMaterial, ? extends TrimMaterialRegistryEntry.Builder>> value);

    TrimPattern createTrimPattern(Consumer<RegistryBuilderFactory<TrimPattern, ? extends TrimPatternRegistryEntry.Builder>> value);
}
