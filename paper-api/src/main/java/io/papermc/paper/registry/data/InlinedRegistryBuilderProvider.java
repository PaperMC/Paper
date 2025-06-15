package io.papermc.paper.registry.data;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@ApiStatus.NonExtendable
public interface InlinedRegistryBuilderProvider {

    static InlinedRegistryBuilderProvider instance() {
        final class Holder {
            static final Optional<InlinedRegistryBuilderProvider> INSTANCE = ServiceLoader.load(InlinedRegistryBuilderProvider.class).findFirst();
        }
        return Holder.INSTANCE.orElseThrow();
    }

    Dialog createDialog(Consumer<RegistryBuilderFactory<Dialog, ? extends DialogRegistryEntry.Builder>> value);
}
