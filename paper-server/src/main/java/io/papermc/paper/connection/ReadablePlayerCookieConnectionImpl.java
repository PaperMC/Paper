package io.papermc.paper.connection;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
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
        this.requestedCookies.computeIfAbsent(id, ignored -> new ConcurrentLinkedQueue<>())
                .add(new CookieFuture(id, future));

        this.connection.send(new ClientboundCookieRequestPacket(id));

        return future;
    }

    public boolean handleCookieResponse(ServerboundCookieResponsePacket packet) {
        AtomicReference<CookieFuture> future = new AtomicReference<>();
        this.requestedCookies.computeIfPresent(packet.key(), (key, queue) -> {
            CookieFuture queued = queue.poll();
            if (queued != null) {
                future.set(queued);
            }
            return queue.isEmpty() ? null : queue;
        });
        CookieFuture queued = future.get();
        if (queued != null) {
            queued.future().complete(packet.payload());
            return true;
        }

        return false;
    }

    public boolean isAwaitingCookies() {
        return !this.requestedCookies.isEmpty();
    }

    public record CookieFuture(Identifier key, CompletableFuture<byte[]> future) {
    }
}
