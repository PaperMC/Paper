package io.papermc.paper.connection;

import org.bukkit.NamespacedKey;

public interface WritablePlayerCookieConnection extends PlayerConnection {

    /**
     * Stores a cookie in this player's client.
     *
     * @param key the key identifying the cookie
     * @param value the data to store in the cookie
     * @throws IllegalStateException if a cookie cannot be stored at this time
     */
    void storeCookie(NamespacedKey key, byte[] value);
}
