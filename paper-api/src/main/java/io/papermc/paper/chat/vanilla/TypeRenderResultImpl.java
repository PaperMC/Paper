package io.papermc.paper.chat.vanilla;

import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

@ApiStatus.Internal
record TypeRenderResultImpl(ChatType.Bound boundChat, @Nullable Component unsignedContent) implements ChatTypeRenderResult {
}
