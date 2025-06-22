package io.papermc.paper.registry.data.dialog.input;

import io.papermc.paper.registry.data.dialog.input.type.DialogInputConfig;

record DialogInputImpl(String key, DialogInputConfig inputType) implements DialogInput {
}
