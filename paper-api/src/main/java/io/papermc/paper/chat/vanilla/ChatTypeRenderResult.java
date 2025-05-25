package io.papermc.paper.chat.vanilla;

import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The result of rendering a chat message into a specific {@link ChatType}.
 */
@NullMarked
@ApiStatus.NonExtendable
public sealed interface ChatTypeRenderResult permits TypeRenderResultImpl {

    /**
     * Create a render result with no unsigned content component.
     *
     * @param bound the {@link ChatType.Bound} containing the formatted message
     *              component and narration text
     * @return a new {@link ChatTypeRenderResult}
     */
    static ChatTypeRenderResult of(ChatType.Bound bound) {
        return new TypeRenderResultImpl(bound, null);
    }

    /**
     * Create a render result including an unsigned content component.
     *
     * @param bound     the {@link ChatType.Bound}
     * @param unsignedContent an optional {@link Component} representing unsigned content, or {@code null}
     * @return a new {@link ChatTypeRenderResult}
     */
    static ChatTypeRenderResult of(ChatType.Bound bound, @Nullable Component unsignedContent) {
        return new TypeRenderResultImpl(bound, unsignedContent);
    }

    /**
     * Retrieves the optional unsigned content component.
     *
     * @return the unsigned {@link Component}, or {@code null} if none was provided
     */
    @Nullable
    Component unsignedContent();

    /**
     * Retrieves the bound chat type result.
     *
     * @return the {@link ChatType.Bound} instance for this render
     */
    ChatType.Bound boundChat();

}
