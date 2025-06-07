package io.papermc.paper.registry.data.dialog.input.type;

import org.jspecify.annotations.Nullable;

record MultilineOptionsImpl(@Nullable Integer maxLines, @Nullable Integer height) implements TextDialogInputType.MultilineOptions {
}
