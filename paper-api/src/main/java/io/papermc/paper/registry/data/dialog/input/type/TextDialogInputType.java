package io.papermc.paper.registry.data.dialog.input.type;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

public sealed interface TextDialogInputType extends DialogInputType permits TextDialogInputTypeImpl {
    int width();

    Component label();

    boolean labelVisible();

    String initial();

    int maxLength();

    @Nullable MultilineOptions multiline();

    sealed interface MultilineOptions permits MultilineOptionsImpl {

        static MultilineOptions create(final @Nullable Integer maxLines, final @Nullable Integer height) {
            return new MultilineOptionsImpl(maxLines, height);
        }

        @Nullable Integer maxLines();

        @Nullable Integer height();
    }
}
