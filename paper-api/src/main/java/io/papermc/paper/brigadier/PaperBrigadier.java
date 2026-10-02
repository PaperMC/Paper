package io.papermc.paper.brigadier;

import com.mojang.brigadier.Message;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

/// Helper methods to bridge the gaps between Brigadier and Paper-MojangAPI.
///
/// @deprecated for removal. See [MessageComponentSerializer] for a direct replacement of functionality found in
/// this class.
/// As a general entrypoint to brigadier on paper, see [io.papermc.paper.command.brigadier.Commands].
@Deprecated(forRemoval = true, since = "1.20.6")
public final class PaperBrigadier {
    private PaperBrigadier() {
        throw new RuntimeException("PaperBrigadier is not to be instantiated!");
    }

    /// Create a new Brigadier [Message] from a [ComponentLike].
    ///
    /// Mostly useful for creating rich suggestion tooltips in combination with other Paper-MojangAPI APIs.
    ///
    /// @param componentLike The [ComponentLike] to use for the [Message] contents
    /// @return A new Brigadier [Message]
    public static @NonNull Message message(final @NonNull ComponentLike componentLike) {
        return MessageComponentSerializer.message().serialize(componentLike.asComponent());
    }

    /// Create a new [Component] from a Brigadier [Message].
    ///
    /// If the [Message] was created from a [Component], it will simply be
    /// converted back, otherwise a new [TextComponent] will be created with the
    /// content of [Message#getString()]
    ///
    /// @param message The [Message] to create a [Component] from
    /// @return The created [Component]
    public static @NonNull Component componentFromMessage(final @NonNull Message message) {
        return MessageComponentSerializer.message().deserialize(message);
    }
}
