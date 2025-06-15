package io.papermc.paper.registry.data.dialog.input.type;

import java.util.List;
import net.kyori.adventure.text.Component;

record SingleOptionDialogInputTypeImpl(int width, List<SingleOptionDialogInputType.OptionEntry> entries, Component label, boolean labelVisible) implements SingleOptionDialogInputType {

    SingleOptionDialogInputTypeImpl {
        entries = List.copyOf(entries);
    }
}
