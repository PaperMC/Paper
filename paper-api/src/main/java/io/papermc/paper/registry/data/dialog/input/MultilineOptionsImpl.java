package io.papermc.paper.registry.data.dialog.input;

import org.jspecify.annotations.Nullable;

record MultilineOptionsImpl(@Nullable Integer maxLines, @Nullable Integer height) implements TextDialogInput.MultilineOptions {
}
