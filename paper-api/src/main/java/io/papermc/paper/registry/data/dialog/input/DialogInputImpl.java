package io.papermc.paper.registry.data.dialog.input;

import io.papermc.paper.registry.data.dialog.input.type.DialogInputType;

record DialogInputImpl(String key, DialogInputType inputType) implements DialogInput {
}
