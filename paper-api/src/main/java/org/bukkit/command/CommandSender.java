package org.bukkit.command;

import java.util.UUID;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandSender extends net.kyori.adventure.audience.Audience, Permissible { // Paper

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     * @see #sendMessage(net.kyori.adventure.text.Component)
     * @see #sendPlainMessage(String)
     * @see #sendRichMessage(String)
     */
    @org.jetbrains.annotations.ApiStatus.Obsolete // Paper
    public void sendMessage(@NotNull String message);

    /**
     * Sends this sender multiple messages
     *
     * @param messages An array of messages to be displayed
     * @see #sendMessage(net.kyori.adventure.text.Component)
     * @see #sendPlainMessage(String)
     * @see #sendRichMessage(String)
     */
    @org.jetbrains.annotations.ApiStatus.Obsolete // Paper
    public void sendMessage(@NotNull String... messages);

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     * @param sender The sender of this message
     * @see #sendMessage(net.kyori.adventure.identity.Identified, net.kyori.adventure.text.Component)
     * @deprecated sender UUID is ignored
     */
    @Deprecated // Paper
    public void sendMessage(@Nullable UUID sender, @NotNull String message);

    /**
     * Sends this sender multiple messages
     *
     * @param messages An array of messages to be displayed
     * @param sender The sender of this message
     * @see #sendMessage(net.kyori.adventure.identity.Identified, net.kyori.adventure.text.Component)
     * @deprecated sender UUID is ignored
     */
    @Deprecated // Paper
    public void sendMessage(@Nullable UUID sender, @NotNull String... messages);

    /**
     * Returns the server instance that this command is running on
     *
     * @return Server instance
     */
    @NotNull
    public Server getServer();

    /**
     * Gets the name of this command sender
     *
     * @return Name of the sender
     */
    @NotNull
    public String getName();

    // Spigot start
    public class Spigot {

        /**
         * Sends this sender a chat component.
         *
         * @param component the components to send
         * @deprecated use {@code sendMessage} methods that accept {@link net.kyori.adventure.text.Component}
         */
        @Deprecated // Paper
        public void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends an array of components as a single message to the sender.
         *
         * @param components the components to send
         * @deprecated use {@code sendMessage} methods that accept {@link net.kyori.adventure.text.Component}
         */
        @Deprecated // Paper
        public void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends this sender a chat component.
         *
         * @param component the components to send
         * @param sender the sender of the message
         * @deprecated use {@code sendMessage} methods that accept {@link net.kyori.adventure.text.Component}
         */
        @Deprecated // Paper
        public void sendMessage(@Nullable UUID sender, @NotNull net.md_5.bungee.api.chat.BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends an array of components as a single message to the sender.
         *
         * @param components the components to send
         * @param sender the sender of the message
         * @deprecated use {@code sendMessage} methods that accept {@link net.kyori.adventure.text.Component}
         */
        @Deprecated // Paper
        public void sendMessage(@Nullable UUID sender, @NotNull net.md_5.bungee.api.chat.BaseComponent... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @NotNull
    Spigot spigot();
    // Spigot end

    // Paper start
    /**
     * Gets the name of this command sender
     *
     * @return Name of the sender
     */
    public net.kyori.adventure.text.@NotNull Component name();

    @Override
    default void sendMessage(final net.kyori.adventure.identity.@NotNull Identity identity, final net.kyori.adventure.text.@NotNull Component message, final net.kyori.adventure.audience.@NotNull MessageType type) {
        this.sendMessage(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(message));
    }

    /**
     * Sends a message with the MiniMessage format to the command sender.
     * <p>
     * See <a href="https://docs.advntr.dev/minimessage/">MiniMessage docs</a>
     * for more information on the format.
     *
     * @param message MiniMessage content
     */
    default void sendRichMessage(final @NotNull String message) {
        this.sendMessage(net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(message, this));
    }

    /**
     * Sends a message with the MiniMessage format to the command sender.
     * <p>
     * See <a href="https://docs.advntr.dev/minimessage/">MiniMessage docs</a> and <a href="https://docs.advntr.dev/minimessage/dynamic-replacements">MiniMessage Placeholders docs</a>
     * for more information on the format.
     *
     * @param message MiniMessage content
     * @param resolvers resolvers to use
     */
    default void sendRichMessage(final @NotNull String message, final net.kyori.adventure.text.minimessage.tag.resolver.@NotNull TagResolver... resolvers) {
        this.sendMessage(net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(message, this, resolvers));
    }

    /**
     * Sends a plain message to the command sender.
     *
     * @param message plain message
     */
    default void sendPlainMessage(final @NotNull String message) {
        this.sendMessage(net.kyori.adventure.text.Component.text(message));
    }

    /**
     * Sends the component to the sender
     *
     * <p>If this sender does not support sending full components then
     * the component will be sent as legacy text.</p>
     *
     * @param component the component to send
     * @deprecated use {@link #sendMessage(Identity, Component, MessageType)} instead
     */
    @Deprecated
    default void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent component) {
        this.sendMessage(component.toLegacyText());
    }

    /**
     * Sends an array of components as a single message to the sender
     *
     * <p>If this sender does not support sending full components then
     * the components will be sent as legacy text.</p>
     *
     * @param components the components to send
     * @deprecated use {@link #sendMessage(Identity, Component, MessageType)} instead
     */
    @Deprecated
    default void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent... components) {
        this.sendMessage(new net.md_5.bungee.api.chat.TextComponent(components).toLegacyText());
    }
    // Paper end
}
