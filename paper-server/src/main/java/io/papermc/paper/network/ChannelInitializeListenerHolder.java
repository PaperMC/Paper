package io.papermc.paper.network;

import io.netty.channel.Channel;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Internal API to register channel initialization listeners.
 * <p>
 * This is not officially supported API and we make no guarantees to the existence or state of this class.
 */
public final class ChannelInitializeListenerHolder {

    private static final Map<Key, ChannelInitializeListener> LISTENERS = new HashMap<>();
    private static final Map<Key, ChannelInitializeListener> IMMUTABLE_VIEW = Collections.unmodifiableMap(LISTENERS);

    private ChannelInitializeListenerHolder() {
    }

    /**
     * Registers whether an initialization listener is registered under the given key.
     *
     * @param key key
     * @return whether an initialization listener is registered under the given key
     */
    public static boolean hasListener(@NonNull Key key) {
        return LISTENERS.containsKey(key);
    }

    /**
     * Registers a channel initialization listener called after ServerConnection is initialized.
     *
     * @param key      key
     * @param listener initialization listeners
     */
    public static void addListener(@NonNull Key key, @NonNull ChannelInitializeListener listener) {
        LISTENERS.put(key, listener);
    }

    /**
     * Removes and returns an initialization listener registered by the given key if present.
     *
     * @param key key
     * @return removed initialization listener if present
     */
    public static @Nullable ChannelInitializeListener removeListener(@NonNull Key key) {
        return LISTENERS.remove(key);
    }

    /**
     * Returns an immutable map of registered initialization listeners.
     *
     * @return immutable map of registered initialization listeners
     */
    public static @NonNull Map<Key, ChannelInitializeListener> getListeners() {
        return IMMUTABLE_VIEW;
    }

    /**
     * Calls the registered listeners with the given channel.
     *
     * @param channel channel
     */
    public static void callListeners(@NonNull Channel channel) {
        for (ChannelInitializeListener listener : LISTENERS.values()) {
            listener.afterInitChannel(channel);
        }
    }
}
