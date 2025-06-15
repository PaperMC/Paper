package io.papermc.paper.registry.data.dialog.input.type;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public sealed interface NumberRangeDialogInputType extends DialogInputType permits NumberRangeDialogInputTypeImpl {

    int width();

    Component label();

    String labelFormat();

    float start();

    float end();

    @Nullable Float initial();

    @Nullable Float step();
}
