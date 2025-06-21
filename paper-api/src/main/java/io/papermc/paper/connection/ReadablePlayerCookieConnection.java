package io.papermc.paper.connection;

import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;
import java.util.concurrent.CompletableFuture;

@NullMarked
public interface ReadablePlayerCookieConnection extends PlayerConnection {

    /**
     * Retrieves a cookie from this connection.
     *
     * @param key the key identifying the cookie
     * @return a {@link CompletableFuture} that will be completed when the
     * Cookie response is received or otherwise available. If the cookie is not
     * set in the client, the {@link CompletableFuture} will complete with a
     * null value.
     */
    CompletableFuture<byte[]> retrieveCookie(NamespacedKey key);

}
