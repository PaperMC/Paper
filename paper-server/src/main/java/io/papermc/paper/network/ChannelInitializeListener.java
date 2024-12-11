package io.papermc.paper.network;

import io.netty.channel.Channel;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Internal API to register channel initialization listeners.
 * <p>
 * This is not officially supported API and we make no guarantees to the existence or state of this interface.
 */
@FunctionalInterface
public interface ChannelInitializeListener {

    void afterInitChannel(@NonNull Channel channel);
}
