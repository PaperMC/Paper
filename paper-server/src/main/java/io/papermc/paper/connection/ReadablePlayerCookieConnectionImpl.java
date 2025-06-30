package io.papermc.paper.connection;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.cookie.ClientboundCookieRequestPacket;
import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class ReadablePlayerCookieConnectionImpl implements ReadablePlayerCookieConnection {

    // Because we support async cookies, order is not promised.
    private final Map<ResourceLocation, CookieFuture> requestedCookies = new ConcurrentHashMap<>();
    private final Connection connection;

    public ReadablePlayerCookieConnectionImpl(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public CompletableFuture<byte[]> retrieveCookie(final NamespacedKey key) {
        Preconditions.checkArgument(key != null, "Cookie key cannot be null");

        CompletableFuture<byte[]> future = new CompletableFuture<>();
        ResourceLocation resourceLocation = CraftNamespacedKey.toMinecraft(key);
        this.requestedCookies.put(resourceLocation, new CookieFuture(resourceLocation, future));

        this.connection.send(new ClientboundCookieRequestPacket(resourceLocation));

        return future;
    }

    public boolean canStoreCookie() {
        return true;
    }

    public boolean handleCookieResponse(ServerboundCookieResponsePacket packet) {
        CookieFuture future = this.requestedCookies.get(packet.key());
        if (future != null) {
            future.future().complete(packet.payload());
            this.requestedCookies.remove(packet.key());
            return true;
        }

        return false;
    }

    public boolean isAwaitingCookies() {
        return !this.requestedCookies.isEmpty();
    }

    public record CookieFuture(ResourceLocation key, CompletableFuture<byte[]> future) {
    }
}
