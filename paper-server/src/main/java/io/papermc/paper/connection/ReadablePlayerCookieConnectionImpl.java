package io.papermc.paper.connection;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.cookie.ClientboundCookieRequestPacket;
import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
import net.minecraft.resources.Identifier;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class ReadablePlayerCookieConnectionImpl implements ReadablePlayerCookieConnection {

    // Because we support async cookies, order is not promised.
    // Requests for the same key are queued, the client answers them in the order they were sent.
    private final Map<Identifier, Queue<CookieFuture>> requestedCookies = new ConcurrentHashMap<>();
    private final Connection connection;

    public ReadablePlayerCookieConnectionImpl(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public CompletableFuture<byte[]> retrieveCookie(final NamespacedKey key) {
        Preconditions.checkArgument(key != null, "Cookie key cannot be null");

        CompletableFuture<byte[]> future = new CompletableFuture<>();
        Identifier id = CraftNamespacedKey.toMinecraft(key);
        this.requestedCookies.compute(id, (ignored, queue) -> {
            if (queue == null) {
                queue = new ArrayDeque<>();
            }
            queue.add(new CookieFuture(id, future));
            return queue;
        });

        this.connection.send(new ClientboundCookieRequestPacket(id));

        return future;
    }

    public boolean handleCookieResponse(ServerboundCookieResponsePacket packet) {
        final CookieFuture[] next = new CookieFuture[1];
        this.requestedCookies.computeIfPresent(packet.key(), (ignored, queue) -> {
            next[0] = queue.poll();
            return queue.isEmpty() ? null : queue;
        });

        if (next[0] == null) {
            return false;
        }

        // Complete outside of the map operation, a callback may request the same cookie again
        next[0].future().complete(packet.payload());
        return true;
    }

    public boolean isAwaitingCookies() {
        return !this.requestedCookies.isEmpty();
    }

    public record CookieFuture(Identifier key, CompletableFuture<byte[]> future) {
    }
}
