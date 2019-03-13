package org.bukkit.plugin.messaging;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Contains information about a {@link Plugin}s registration to a plugin
 * channel.
 */
public final class PluginMessageListenerRegistration {
    private final Messenger messenger;
    private final Plugin plugin;
    private final String channel;
    private final PluginMessageListener listener;

    public PluginMessageListenerRegistration(@NotNull Messenger messenger, @NotNull Plugin plugin, @NotNull String channel, @NotNull PluginMessageListener listener) {
        if (messenger == null) {
            throw new IllegalArgumentException("Messenger cannot be null!");
        }
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null!");
        }
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null!");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null!");
        }

        this.messenger = messenger;
        this.plugin = plugin;
        this.channel = channel;
        this.listener = listener;
    }

    /**
     * Gets the plugin channel that this registration is about.
     *
     * @return Plugin channel.
     */
    @NotNull
    public String getChannel() {
        return channel;
    }

    /**
     * Gets the registered listener described by this registration.
     *
     * @return Registered listener.
     */
    @NotNull
    public PluginMessageListener getListener() {
        return listener;
    }

    /**
     * Gets the plugin that this registration is for.
     *
     * @return Registered plugin.
     */
    @NotNull
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Checks if this registration is still valid.
     *
     * @return True if this registration is still valid, otherwise false.
     */
    public boolean isValid() {
        return messenger.isRegistrationValid(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PluginMessageListenerRegistration other = (PluginMessageListenerRegistration) obj;
        if (this.messenger != other.messenger && !this.messenger.equals(other.messenger)) {
            return false;
        }
        if (this.plugin != other.plugin && !this.plugin.equals(other.plugin)) {
            return false;
        }
        if (!this.channel.equals(other.channel)) {
            return false;
        }
        if (this.listener != other.listener && !this.listener.equals(other.listener)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.messenger.hashCode();
        hash = 53 * hash + this.plugin.hashCode();
        hash = 53 * hash + this.channel.hashCode();
        hash = 53 * hash + this.listener.hashCode();
        return hash;
    }
}
