package com.destroystokyo.paper.event.player;

import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * This event is fired during a player handshake.
 * <p>
 * If there are no listeners listening to this event, the logic default
 * to your server platform will be run.
 *
 * <p>WARNING: TAMPERING WITH THIS EVENT CAN BE DANGEROUS</p>
 */
public interface PlayerHandshakeEvent extends Event, Cancellable {

    /**
     * Gets the original handshake string.
     *
     * @return the original handshake string
     */
    String getOriginalHandshake();

    /**
     * Gets the original socket address hostname.
     *
     * <p>This does not include the port.</p>
     * <p>In cases where this event is manually fired and the plugin wasn't updated yet, the default is {@code "127.0.0.1"}.</p>
     *
     * @return the original socket address hostname
     */
    String getOriginalSocketAddressHostname();

    /**
     * Gets the server hostname string.
     *
     * <p>This should not include the port.</p>
     *
     * @return the server hostname string
     */
    @Nullable String getServerHostname();

    /**
     * Sets the server hostname string.
     *
     * <p>This should not include the port.</p>
     *
     * @param serverHostname the server hostname string
     */
    void setServerHostname(String serverHostname);

    /**
     * Gets the socket address hostname string.
     *
     * <p>This should not include the port.</p>
     *
     * @return the socket address hostname string
     */
    @Nullable String getSocketAddressHostname();

    /**
     * Sets the socket address hostname string.
     *
     * <p>This should not include the port.</p>
     *
     * @param socketAddressHostname the socket address hostname string
     */
    void setSocketAddressHostname(String socketAddressHostname);

    /**
     * Gets the unique id.
     *
     * @return the unique id
     */
    @Nullable UUID getUniqueId();

    /**
     * Sets the unique id.
     *
     * @param uniqueId the unique id
     */
    void setUniqueId(UUID uniqueId);

    /**
     * Gets the profile properties.
     *
     * <p>This should be a valid JSON string.</p>
     *
     * @return the profile properties, as JSON
     */
    @Nullable String getPropertiesJson();

    /**
     * Determines if authentication failed.
     * <p>
     * When {@code true}, the client connecting will be disconnected
     * with the {@link #getFailMessage() fail message}.
     *
     * @return {@code true} if authentication failed, {@code false} otherwise
     */
    boolean isFailed();

    /**
     * Sets if authentication failed and the client should be disconnected.
     * <p>
     * When {@code true}, the client connecting will be disconnected
     * with the {@link #getFailMessage() fail message}.
     *
     * @param failed {@code true} if authentication failed, {@code false} otherwise
     */
    void setFailed(boolean failed);

    /**
     * Sets the profile properties.
     *
     * <p>This should be a valid JSON string.</p>
     *
     * @param propertiesJson the profile properties, as JSON
     */
    void setPropertiesJson(String propertiesJson);

    /**
     * Gets the message to display to the client when authentication fails.
     *
     * @return the message to display to the client
     */
    Component failMessage();

    /**
     * Sets the message to display to the client when authentication fails.
     *
     * @param failMessage the message to display to the client
     */
    void failMessage(Component failMessage);

    /**
     * Gets the message to display to the client when authentication fails.
     *
     * @return the message to display to the client
     * @deprecated use {@link #failMessage()}
     */
    @Deprecated
    String getFailMessage();

    /**
     * Sets the message to display to the client when authentication fails.
     *
     * @param failMessage the message to display to the client
     * @deprecated use {@link #failMessage(Component)}
     */
    @Deprecated
    void setFailMessage(String failMessage);

    /**
     * {@inheritDoc}
     * <p>
     * When this event is cancelled, custom handshake logic will not
     * be processed.
     */
    @Override
    boolean isCancelled();

    /**
     * {@inheritDoc}
     * <p>
     * When this event is cancelled, custom handshake logic will not
     * be processed.
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
