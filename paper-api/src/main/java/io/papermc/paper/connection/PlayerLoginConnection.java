package io.papermc.paper.connection;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

@NullMarked
public interface PlayerLoginConnection extends CookieConnection  {

    /**
     * Gets the authenticated profile for this connection.
     * This may return null depending on what stage this connection is at.
     * @return authenticated profile, or null if not currently present
     */
    @Nullable
    PlayerProfile getAuthenticatedProfile();

    /**
     * Gets the player profile that this connection is requesting to authenticate as.
     * @return the unsafe unauthenticated profile, or null if not currently sent
     */
    @Nullable
    PlayerProfile getUnsafeProfile();

    /**
     * Gets the player IP address.
     *
     * @return The IP address
     */
    @NotNull
    InetAddress getAddress();

    /**
     * Gets the raw address of the player logging in
     * @return The address
     */
    @NotNull
    InetAddress getRawAddress();

    /**
     * Gets the hostname that the player used to connect to the server, or
     * blank if unknown
     *
     * @return The hostname
     */
    @NotNull
    String getHostname();

    // TODO: Should we have a read-only interface?
    @Deprecated
    @Override
    void storeCookie(NamespacedKey key, byte[] value) throws UnsupportedOperationException;
}
