package io.papermc.paper.chat.vanilla;

import io.papermc.paper.InternalAPIBridge;
import io.papermc.paper.chat.ChatTypeParameter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import java.util.List;

/**
 * Represents a renderer implementation that utilizes Minecraft's {@link ChatType} system for
 * rendering on the client.
 * <p>
 * Using this API allows additional flexibility such as configurable narration.
 * <p>
 * Implementations are either viewer-unaware or viewer-aware, depending on
 * whether they need to know the audience when rendering.
 * </p>
 */
@ApiStatus.NonExtendable
public sealed interface ChatTypeRenderer permits ChatTypeRenderer.ViewerUnaware, ChatTypeRenderer.ViewerAware {

    /**
     * A chat type renderer that does not consider the viewer when rendering.
     */
    non-sealed interface ViewerUnaware extends ChatTypeRenderer {

        /**
         * Renders a chat message without regard to a recipient.
         *
         * @param source the player who sent the message
         * @param sourceDisplayName the display name component of the sender
         * @param message the message content component
         * @return the rendering result
         */
        @ApiStatus.OverrideOnly
        ChatTypeRenderResult render(Player source, Component sourceDisplayName, Component message);

    }

    /**
     * A chat type renderer that may customize output based on the viewer.
     */
    non-sealed interface ViewerAware extends ChatTypeRenderer {

        /**
         * Render a chat message while taking into account who is receiving the message.
         *
         * @param source the player who sent the message
         * @param sourceDisplayName the display name component of the sender
         * @param message the message content component
         * @param viewer the audience receiving the message
         * @return the rendering result
         */
        @ApiStatus.OverrideOnly
        ChatTypeRenderResult render(Player source, Component sourceDisplayName, Component message, Audience viewer);

    }

    /**
     * Creates a vanilla {@link ChatType} with custom message formatting and narration.
     * <p>
     * The position of each {@link ChatTypeParameter} directly determines which
     * placeholder it substitutes in both the chat message and the narration output.
     *
     * @param format     the pattern for rendering chat messages (e.g. "<%s> %s")
     * @param narration  the pattern for the narration of  chat messages (e.g. "%s says %s")
     * @param style      the {@link Style} to apply to the rendered message
     * @param parameters an ordered list of {@link ChatTypeParameter}s
     *
     * @return a new {@link ChatType} instance with the specified format, narration,style, and parameters
     */
    static ChatType vanillaChatType(String format, String narration, Style style, List<ChatTypeParameter> parameters) {
        return InternalAPIBridge.get().createAdventureChatType(format, narration, style, parameters);
    }

}
