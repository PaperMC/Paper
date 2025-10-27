package io.papermc.paper.network;

import io.netty.channel.Channel;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Internal API to register channel initialization listeners.
 *
 * Improvements over the original:
 * <ul>
 *     <li>Thread-safe modifications while preserving insertion order</li>
 *     <li>Snapshot-based iteration to avoid concurrent-modification issues</li>
 *     <li>Optional concurrent invocation of listeners (configurable)</li>
 *     <li>Robust per-listener exception handling with logging</li>
 *     <li>Immutable snapshot returned by {@link #getListeners()}</li>
 * </ul>
 *
 * Note: listeners are expected to be non-blocking. If you enable concurrent invocation,
 * listeners will be invoked in parallel using the stream parallelism of the JVM (ForkJoinPool.commonPool()).
 * If listeners may block for significant time, prefer sequential invocation or provide your own executor.
 */
public final class ChannelInitializeListenerHolder {

    private static final Logger LOGGER = Logger.getLogger(ChannelInitializeListenerHolder.class.getName());

    /**
     * Preserve insertion order using LinkedHashMap.
     * All accesses to LISTENERS must be guarded by LOCK.
     */
    private static final Map<Key, ChannelInitializeListener> LISTENERS = new LinkedHashMap<>();
    private static final Object LOCK = new Object();

    /**
     * When true, {@link #callListeners(Channel)} will invoke listener callbacks concurrently (parallel stream).
     * Default: false (sequential invocation, preserving insertion order semantics).
     */
    private static volatile boolean concurrentInvocation = false;

    private ChannelInitializeListenerHolder() {
    }

    /**
     * Sets whether listeners should be invoked concurrently when {@link #callListeners(Channel)} is called.
     *
     * @param concurrent true to enable concurrent invocation, false to invoke sequentially (in insertion order)
     */
    public static void setConcurrentInvocation(boolean concurrent) {
        concurrentInvocation = concurrent;
    }

    /**
     * Returns whether listeners are invoked concurrently.
     *
     * @return true if concurrent invocation is enabled
     */
    public static boolean isConcurrentInvocation() {
        return concurrentInvocation;
    }

    /**
     * Returns whether an initialization listener is registered under the given key.
     *
     * @param key key
     * @return whether an initialization listener is registered under the given key
     */
    public static boolean hasListener(@NonNull Key key) {
        Objects.requireNonNull(key, "key");
        synchronized (LOCK) {
            return LISTENERS.containsKey(key);
        }
    }

    /**
     * Registers a channel initialization listener called after ServerConnection is initialized.
     * If a listener already existed for the provided key, it will be replaced and the previous instance returned.
     *
     * @param key      key
     * @param listener initialization listener
     * @return previous listener mapped to the key, or null if none
     */
    public static @Nullable ChannelInitializeListener addListener(@NonNull Key key, @NonNull ChannelInitializeListener listener) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(listener, "listener");
        synchronized (LOCK) {
            return LISTENERS.put(key, listener);
        }
    }

    /**
     * Registers a channel initialization listener only if absent.
     *
     * @param key      key
     * @param listener initialization listener
     * @return the existing listener if present, otherwise null (and the listener is registered)
     */
    public static @Nullable ChannelInitializeListener registerIfAbsent(@NonNull Key key, @NonNull ChannelInitializeListener listener) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(listener, "listener");
        synchronized (LOCK) {
            ChannelInitializeListener existing = LISTENERS.get(key);
            if (existing == null) {
                LISTENERS.put(key, listener);
            }
            return existing;
        }
    }

    /**
     * Removes and returns an initialization listener registered by the given key if present.
     *
     * @param key key
     * @return removed initialization listener if present
     */
    public static @Nullable ChannelInitializeListener removeListener(@NonNull Key key) {
        Objects.requireNonNull(key, "key");
        synchronized (LOCK) {
            return LISTENERS.remove(key);
        }
    }

    /**
     * Clears all registered listeners.
     * Use carefully; typically only for tests or controlled reloads.
     */
    public static void clear() {
        synchronized (LOCK) {
            LISTENERS.clear();
        }
    }

    /**
     * Returns an immutable snapshot of registered initialization listeners, preserving insertion order.
     * Mutating the returned map will throw {@link UnsupportedOperationException}.
     *
     * @return immutable snapshot of registered initialization listeners
     */
    public static @NonNull Map<Key, ChannelInitializeListener> getListeners() {
        synchronized (LOCK) {
            // Return a shallow copy snapshot wrapped as unmodifiable to avoid exposing internal map.
            return Collections.unmodifiableMap(new LinkedHashMap<>(LISTENERS));
        }
    }

    /**
     * Calls the registered listeners with the given channel.
     *
     * This method creates a snapshot of the current listeners (to avoid concurrent modification
     * or holding the internal lock during listener invocation). Each listener is invoked
     * with robust per-listener exception handling so one faulty listener does not stop the rest.
     *
     * If concurrent invocation is enabled via {@link #setConcurrentInvocation(boolean)}, listeners
     * will be invoked in parallel using the common ForkJoinPool; otherwise they are invoked
     * sequentially in insertion order.
     *
     * @param channel channel
     */
    public static void callListeners(@NonNull Channel channel) {
        Objects.requireNonNull(channel, "channel");

        final List<ChannelInitializeListener> snapshot;
        synchronized (LOCK) {
            if (LISTENERS.isEmpty()) {
                return;
            }
            // Keep insertion order
            snapshot = new ArrayList<>(LISTENERS.values());
        }

        if (concurrentInvocation) {
            // Parallel invocation: each listener is invoked independently. Order is not guaranteed.
            snapshot.parallelStream().forEach(listener -> safeInvoke(listener, channel));
        } else {
            // Sequential invocation (in insertion order)
            for (ChannelInitializeListener listener : snapshot) {
                safeInvoke(listener, channel);
            }
        }
    }

    private static void safeInvoke(ChannelInitializeListener listener, Channel channel) {
        try {
            listener.afterInitChannel(channel);
        } catch (Throwable t) {
            // Protect server from misbehaving listeners — log and continue.
            LOGGER.log(Level.WARNING, "Exception thrown from ChannelInitializeListener: " + listener + " for channel " + channel, t);
        }
    }
}
