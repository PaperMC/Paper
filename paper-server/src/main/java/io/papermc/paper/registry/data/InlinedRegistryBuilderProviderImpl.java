package io.papermc.paper.registry.data;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.function.Consumer;

public final class InlinedRegistryBuilderProviderImpl implements InlinedRegistryBuilderProvider {

    @Override
    public Dialog createDialog(final Consumer<RegistryBuilderFactory<Dialog, ? extends DialogRegistryEntry.Builder>> value) {
        return Conversions.global().createApiInstanceFromBuilder(RegistryKey.DIALOG, value);
    }
}
