package io.papermc.paper.connection;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import java.net.InetAddress;
import java.net.InetSocketAddress;

@NullMarked
public interface PlayerConfigurationConnection extends CookieConnection {

    /**
     * Gets the profile for this connection.
     * @return profile
     */
    PlayerProfile getProfile();

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

    boolean isTransferred();

    /**
     * Clears the players chat history and their local chat.
     */
    void clearChat();

    /**
     * Note, this should be only be called if you are reconfiguring the player.
     * Calling this on initial configuration will caused undefined behavior.
     */
    void completeConfiguration();

    <T> T getClientOption(com.destroystokyo.paper.ClientOption<T> type);

    void transfer(String host, int port);
}
