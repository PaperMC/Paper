package io.papermc.paper.registry.data.dialog.body;

import net.kyori.adventure.text.Component;

record PlainMessageBodyImpl(Component contents, int width) implements DialogBody.PlainMessageBody {
}
