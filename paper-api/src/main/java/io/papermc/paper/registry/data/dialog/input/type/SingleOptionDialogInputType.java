package io.papermc.paper.registry.data.dialog.input.type;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public sealed interface SingleOptionDialogInputType extends DialogInputType permits SingleOptionDialogInputTypeImpl {
    int width();

    @Unmodifiable List<OptionEntry> entries();

    Component label();

    boolean labelVisible();

    sealed interface OptionEntry permits SingleOptionEntryImpl {

        static OptionEntry create(final String id, final @Nullable Component display, final boolean initial) {
            return new SingleOptionEntryImpl(id, display, initial);
        }

        String id();

        @Nullable Component display();

        boolean initial();
    }
}
