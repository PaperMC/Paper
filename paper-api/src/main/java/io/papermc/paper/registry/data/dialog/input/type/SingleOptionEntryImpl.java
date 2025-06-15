package io.papermc.paper.registry.data.dialog.input.type;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

record SingleOptionEntryImpl(String id, @Nullable Component display, boolean initial) implements SingleOptionDialogInputType.OptionEntry {
}
