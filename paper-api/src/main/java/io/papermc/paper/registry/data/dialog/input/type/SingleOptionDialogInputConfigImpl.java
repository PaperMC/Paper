package io.papermc.paper.registry.data.dialog.input.type;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

record SingleOptionDialogInputConfigImpl(int width, List<SingleOptionDialogInputConfig.OptionEntry> entries, Component label, boolean labelVisible) implements SingleOptionDialogInputConfig {

    SingleOptionDialogInputConfigImpl {
        entries = List.copyOf(entries);
    }

    record SingleOptionEntryImpl(String id, @Nullable Component display, boolean initial) implements OptionEntry {
    }
}
