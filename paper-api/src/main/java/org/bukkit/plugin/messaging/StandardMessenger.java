package org.bukkit.plugin.messaging;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Standard implementation to {@link Messenger}
 */
public class StandardMessenger implements Messenger {
    private final Map<String, Set<PluginMessageListenerRegistration>> incomingByChannel = new HashMap<String, Set<PluginMessageListenerRegistration>>();
    private final Map<Plugin, Set<PluginMessageListenerRegistration>> incomingByPlugin = new HashMap<Plugin, Set<PluginMessageListenerRegistration>>();
    private final Map<String, Set<Plugin>> outgoingByChannel = new HashMap<String, Set<Plugin>>();
    private final Map<Plugin, Set<String>> outgoingByPlugin = new HashMap<Plugin, Set<String>>();
    private final Object incomingLock = new Object();
    private final Object outgoingLock = new Object();

    private void addToOutgoing(@NotNull Plugin plugin, @NotNull String channel) {
        synchronized (outgoingLock) {
            Set<Plugin> plugins = outgoingByChannel.get(channel);
            Set<String> channels = outgoingByPlugin.get(plugin);

            if (plugins == null) {
                plugins = new HashSet<Plugin>();
                outgoingByChannel.put(channel, plugins);
            }

            if (channels == null) {
                channels = new HashSet<String>();
                outgoingByPlugin.put(plugin, channels);
            }

            plugins.add(plugin);
            channels.add(channel);
        }
    }

    private void removeFromOutgoing(@NotNull Plugin plugin, @NotNull String channel) {
        synchronized (outgoingLock) {
            Set<Plugin> plugins = outgoingByChannel.get(channel);
            Set<String> channels = outgoingByPlugin.get(plugin);

            if (plugins != null) {
                plugins.remove(plugin);

                if (plugins.isEmpty()) {
                    outgoingByChannel.remove(channel);
                }
            }

            if (channels != null) {
                channels.remove(channel);

                if (channels.isEmpty()) {
                    outgoingByChannel.remove(channel);
                }
            }
        }
    }

    private void removeFromOutgoing(@NotNull Plugin plugin) {
        synchronized (outgoingLock) {
            Set<String> channels = outgoingByPlugin.get(plugin);

            if (channels != null) {
                String[] toRemove = channels.toArray(new String[channels.size()]);

                outgoingByPlugin.remove(plugin);

                for (String channel : toRemove) {
                    removeFromOutgoing(plugin, channel);
                }
            }
        }
    }

    private void addToIncoming(@NotNull PluginMessageListenerRegistration registration) {
        synchronized (incomingLock) {
            Set<PluginMessageListenerRegistration> registrations = incomingByChannel.get(registration.getChannel());

            if (registrations == null) {
                registrations = new HashSet<PluginMessageListenerRegistration>();
                incomingByChannel.put(registration.getChannel(), registrations);
            } else {
                if (registrations.contains(registration)) {
                    throw new IllegalArgumentException("This registration already exists");
                }
            }

            registrations.add(registration);

            registrations = incomingByPlugin.get(registration.getPlugin());

            if (registrations == null) {
                registrations = new HashSet<PluginMessageListenerRegistration>();
                incomingByPlugin.put(registration.getPlugin(), registrations);
            } else {
                if (registrations.contains(registration)) {
                    throw new IllegalArgumentException("This registration already exists");
                }
            }

            registrations.add(registration);
        }
    }

    private void removeFromIncoming(@NotNull PluginMessageListenerRegistration registration) {
        synchronized (incomingLock) {
            Set<PluginMessageListenerRegistration> registrations = incomingByChannel.get(registration.getChannel());

            if (registrations != null) {
                registrations.remove(registration);

                if (registrations.isEmpty()) {
                    incomingByChannel.remove(registration.getChannel());
                }
            }

            registrations = incomingByPlugin.get(registration.getPlugin());

            if (registrations != null) {
                registrations.remove(registration);

                if (registrations.isEmpty()) {
                    incomingByPlugin.remove(registration.getPlugin());
                }
            }
        }
    }

    private void removeFromIncoming(@NotNull Plugin plugin, @NotNull String channel) {
        synchronized (incomingLock) {
            Set<PluginMessageListenerRegistration> registrations = incomingByPlugin.get(plugin);

            if (registrations != null) {
                PluginMessageListenerRegistration[] toRemove = registrations.toArray(new PluginMessageListenerRegistration[registrations.size()]);

                for (PluginMessageListenerRegistration registration : toRemove) {
                    if (registration.getChannel().equals(channel)) {
                        removeFromIncoming(registration);
                    }
                }
            }
        }
    }

    private void removeFromIncoming(@NotNull Plugin plugin) {
        synchronized (incomingLock) {
            Set<PluginMessageListenerRegistration> registrations = incomingByPlugin.get(plugin);

            if (registrations != null) {
                PluginMessageListenerRegistration[] toRemove = registrations.toArray(new PluginMessageListenerRegistration[registrations.size()]);

                incomingByPlugin.remove(plugin);

                for (PluginMessageListenerRegistration registration : toRemove) {
                    removeFromIncoming(registration);
                }
            }
        }
    }

    @Override
    public boolean isReservedChannel(@NotNull String channel) {
        channel = validateAndCorrectChannel(channel);

        return channel.contains("minecraft") && !channel.equals("minecraft:brand");
    }

    @Override
    public void registerOutgoingPluginChannel(@NotNull Plugin plugin, @NotNull String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        channel = validateAndCorrectChannel(channel);
        if (isReservedChannel(channel)) {
            throw new ReservedChannelException(channel);
        }

        addToOutgoing(plugin, channel);
    }

    @Override
    public void unregisterOutgoingPluginChannel(@NotNull Plugin plugin, @NotNull String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        channel = validateAndCorrectChannel(channel);

        removeFromOutgoing(plugin, channel);
    }

    @Override
    public void unregisterOutgoingPluginChannel(@NotNull Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }

        removeFromOutgoing(plugin);
    }

    @Override
    @NotNull
    public PluginMessageListenerRegistration registerIncomingPluginChannel(@NotNull Plugin plugin, @NotNull String channel, @NotNull PluginMessageListener listener) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        channel = validateAndCorrectChannel(channel);
        if (isReservedChannel(channel)) {
            throw new ReservedChannelException(channel);
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }

        PluginMessageListenerRegistration result = new PluginMessageListenerRegistration(this, plugin, channel, listener);

        addToIncoming(result);

        return result;
    }

    @Override
    public void unregisterIncomingPluginChannel(@NotNull Plugin plugin, @NotNull String channel, @NotNull PluginMessageListener listener) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        channel = validateAndCorrectChannel(channel);

        removeFromIncoming(new PluginMessageListenerRegistration(this, plugin, channel, listener));
    }

    @Override
    public void unregisterIncomingPluginChannel(@NotNull Plugin plugin, @NotNull String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        channel = validateAndCorrectChannel(channel);

        removeFromIncoming(plugin, channel);
    }

    @Override
    public void unregisterIncomingPluginChannel(@NotNull Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }

        removeFromIncoming(plugin);
    }

    @Override
    @NotNull
    public Set<String> getOutgoingChannels() {
        synchronized (outgoingLock) {
            Set<String> keys = outgoingByChannel.keySet();
            return ImmutableSet.copyOf(keys);
        }
    }

    @Override
    @NotNull
    public Set<String> getOutgoingChannels(@NotNull Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }

        synchronized (outgoingLock) {
            Set<String> channels = outgoingByPlugin.get(plugin);

            if (channels != null) {
                return ImmutableSet.copyOf(channels);
            } else {
                return ImmutableSet.of();
            }
        }
    }

    @Override
    @NotNull
    public Set<String> getIncomingChannels() {
        synchronized (incomingLock) {
            Set<String> keys = incomingByChannel.keySet();
            return ImmutableSet.copyOf(keys);
        }
    }

    @Override
    @NotNull
    public Set<String> getIncomingChannels(@NotNull Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }

        synchronized (incomingLock) {
            Set<PluginMessageListenerRegistration> registrations = incomingByPlugin.get(plugin);

            if (registrations != null) {
                Builder<String> builder = ImmutableSet.builder();

                for (PluginMessageListenerRegistration registration : registrations) {
                    builder.add(registration.getChannel());
                }

                return builder.build();
            } else {
                return ImmutableSet.of();
            }
        }
    }

    @Override
    @NotNull
    public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(@NotNull Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }

        synchronized (incomingLock) {
            Set<PluginMessageListenerRegistration> registrations = incomingByPlugin.get(plugin);

            if (registrations != null) {
                return ImmutableSet.copyOf(registrations);
            } else {
                return ImmutableSet.of();
            }
        }
    }

    @Override
    @NotNull
    public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(@NotNull String channel) {
        channel = validateAndCorrectChannel(channel);

        synchronized (incomingLock) {
            Set<PluginMessageListenerRegistration> registrations = incomingByChannel.get(channel);

            if (registrations != null) {
                return ImmutableSet.copyOf(registrations);
            } else {
                return ImmutableSet.of();
            }
        }
    }

    @Override
    @NotNull
    public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(@NotNull Plugin plugin, @NotNull String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        channel = validateAndCorrectChannel(channel);

        synchronized (incomingLock) {
            Set<PluginMessageListenerRegistration> registrations = incomingByPlugin.get(plugin);

            if (registrations != null) {
                Builder<PluginMessageListenerRegistration> builder = ImmutableSet.builder();

                for (PluginMessageListenerRegistration registration : registrations) {
                    if (registration.getChannel().equals(channel)) {
                        builder.add(registration);
                    }
                }

                return builder.build();
            } else {
                return ImmutableSet.of();
            }
        }
    }

    @Override
    public boolean isRegistrationValid(@NotNull PluginMessageListenerRegistration registration) {
        if (registration == null) {
            throw new IllegalArgumentException("Registration cannot be null");
        }

        synchronized (incomingLock) {
            Set<PluginMessageListenerRegistration> registrations = incomingByPlugin.get(registration.getPlugin());

            if (registrations != null) {
                return registrations.contains(registration);
            }

            return false;
        }
    }

    @Override
    public boolean isIncomingChannelRegistered(@NotNull Plugin plugin, @NotNull String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        channel = validateAndCorrectChannel(channel);

        synchronized (incomingLock) {
            Set<PluginMessageListenerRegistration> registrations = incomingByPlugin.get(plugin);

            if (registrations != null) {
                for (PluginMessageListenerRegistration registration : registrations) {
                    if (registration.getChannel().equals(channel)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    @Override
    public boolean isOutgoingChannelRegistered(@NotNull Plugin plugin, @NotNull String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        channel = validateAndCorrectChannel(channel);

        synchronized (outgoingLock) {
            Set<String> channels = outgoingByPlugin.get(plugin);

            if (channels != null) {
                return channels.contains(channel);
            }

            return false;
        }
    }

    @Override
    public void dispatchIncomingMessage(@NotNull Player source, @NotNull String channel, @NotNull byte[] message) {
        if (source == null) {
            throw new IllegalArgumentException("Player source cannot be null");
        }
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        channel = validateAndCorrectChannel(channel);

        Set<PluginMessageListenerRegistration> registrations = getIncomingChannelRegistrations(channel);

        for (PluginMessageListenerRegistration registration : registrations) {
            try {
                registration.getListener().onPluginMessageReceived(channel, source, message);
            } catch (Throwable t) {
                registration.getPlugin().getLogger().log(Level.WARNING,
                    String.format("Plugin %s generated an exception whilst handling plugin message",
                        registration.getPlugin().getDescription().getFullName()
                    ), t);
            }
        }
    }

    /**
     * Validates a Plugin Channel name.
     *
     * @param channel Channel name to validate.
     * @deprecated not an API method
     */
    @Deprecated
    public static void validateChannel(@NotNull String channel) {
        validateAndCorrectChannel(channel);
    }

    /**
     * Validates and corrects a Plugin Channel name. Method is not reentrant / idempotent.
     *
     * @param channel Channel name to validate.
     * @return corrected channel name
     * @deprecated not an API method
     */
    @Deprecated
    @NotNull
    public static String validateAndCorrectChannel(@NotNull String channel) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null");
        }
        // This will correct registrations / outgoing messages
        // It is not legal to send "BungeeCord" incoming anymore so we are fine there,
        // but we must make sure that none of the API methods repeatedly call validate
        if (channel.equals("BungeeCord")) {
            return "bungeecord:main";
        }
        // And this will correct incoming messages.
        if (channel.equals("bungeecord:main")) {
            return "BungeeCord";
        }
        if (channel.length() > Messenger.MAX_CHANNEL_SIZE) {
            throw new ChannelNameTooLongException(channel);
        }
        if (channel.indexOf(':') == -1) {
            throw new IllegalArgumentException("Channel must contain : separator (attempted to use " + channel + ")");
        }
        if (!channel.toLowerCase(Locale.ROOT).equals(channel)) {
            // TODO: use NamespacedKey validation here
            throw new IllegalArgumentException("Channel must be entirely lowercase (attempted to use " + channel + ")");
        }
        return channel;
    }

    /**
     * Validates the input of a Plugin Message, ensuring the arguments are all
     * valid.
     *
     * @param messenger Messenger to use for validation.
     * @param source Source plugin of the Message.
     * @param channel Plugin Channel to send the message by.
     * @param message Raw message payload to send.
     * @throws IllegalArgumentException Thrown if the source plugin is
     *     disabled.
     * @throws IllegalArgumentException Thrown if source, channel or message
     *     is null.
     * @throws MessageTooLargeException Thrown if the message is too big.
     * @throws ChannelNameTooLongException Thrown if the channel name is too
     *     long.
     * @throws ChannelNotRegisteredException Thrown if the channel is not
     *     registered for this plugin.
     */
    public static void validatePluginMessage(@NotNull Messenger messenger, @NotNull Plugin source, @NotNull String channel, @NotNull byte[] message) {
        if (messenger == null) {
            throw new IllegalArgumentException("Messenger cannot be null");
        }
        if (source == null) {
            throw new IllegalArgumentException("Plugin source cannot be null");
        }
        if (!source.isEnabled()) {
            throw new IllegalArgumentException("Plugin must be enabled to send messages");
        }
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        if (!messenger.isOutgoingChannelRegistered(source, channel)) {
            throw new ChannelNotRegisteredException(channel);
        }
        if (message.length > Messenger.MAX_MESSAGE_SIZE) {
            throw new MessageTooLargeException(message);
        }
        validateChannel(channel);
    }
}
