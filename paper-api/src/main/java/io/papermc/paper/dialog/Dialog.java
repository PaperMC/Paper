package io.papermc.paper.dialog;

import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.data.InlinedRegistryBuilderProvider;
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry;
import java.util.function.Consumer;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface Dialog extends Keyed {

    @ApiStatus.Experimental
    static Dialog create(final Consumer<RegistryBuilderFactory<Dialog, ? extends DialogRegistryEntry.Builder>> value) {
        return InlinedRegistryBuilderProvider.instance().createDialog(value);
    }
}
