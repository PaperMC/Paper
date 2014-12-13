package org.bukkit.command;

import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandSender extends Permissible {

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     */
    public void sendMessage(@NotNull String message);

    /**
     * Sends this sender multiple messages
     *
     * @param messages An array of messages to be displayed
     */
    public void sendMessage(@NotNull String... messages);

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     * @param sender The sender of this message
     */
    public void sendMessage(@Nullable UUID sender, @NotNull String message);

    /**
     * Sends this sender multiple messages
     *
     * @param messages An array of messages to be displayed
     * @param sender The sender of this message
     */
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
         */
        public void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends an array of components as a single message to the sender.
         *
         * @param components the components to send
         */
        public void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends this sender a chat component.
         *
         * @param component the components to send
         * @param sender the sender of the message
         */
        public void sendMessage(@Nullable UUID sender, @NotNull net.md_5.bungee.api.chat.BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends an array of components as a single message to the sender.
         *
         * @param components the components to send
         * @param sender the sender of the message
         */
        public void sendMessage(@Nullable UUID sender, @NotNull net.md_5.bungee.api.chat.BaseComponent... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @NotNull
    Spigot spigot();
    // Spigot end
}
