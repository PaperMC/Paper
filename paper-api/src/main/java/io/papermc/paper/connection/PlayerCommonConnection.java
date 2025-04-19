package io.papermc.paper.connection;

import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.resource.ResourcePackRequestLike;
import org.bukkit.ServerLinks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.UUID;

/**
 * Represents a connection that has properties shared between the GAME and CONFIG stage.
 */
@NullMarked
public interface PlayerCommonConnection extends CookieConnection {

    void sendResourcePacks(final @NotNull ResourcePackRequest request);

    void removeResourcePacks(final @NotNull UUID id, final @NotNull UUID... others);

    void clearResourcePacks();

    /**
     * Sends data to appear in this connection's report logs.
     * This is useful for debugging server state that may be causing
     * player disconnects.
     * <p>
     * These are formatted as key - value, where keys are limited to a length of 128 characters,
     * values are limited to 4096, and 32 maximum entries can be sent.
     *
     * @param details report details
     */
    void sendReportDetails(Map<String, String> details);

    /**
     * Sends the given server links to this connection.
     * @param links
     */
    void sendLinks(ServerLinks links);

    /**
     * Transfers this connection to another server.
     *
     * @param host host
     * @param port port
     */
    void transfer(String host, int port);

    /**
     * Gets the brand of this player.
     * It is generally expected that the brand will be available.
     * @return brand, or null if not present
     */
    @Nullable
    String getBrand();
}
