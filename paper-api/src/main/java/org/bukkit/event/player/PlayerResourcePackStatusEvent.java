package org.bukkit.event.player;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player takes action on a resource pack request sent via
 * {@link Player#setResourcePack(java.lang.String)}.
 */
public class PlayerResourcePackStatusEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final UUID id;
    private final Status status;

    public PlayerResourcePackStatusEvent(@NotNull final Player who, @NotNull UUID id, @NotNull Status resourcePackStatus) {
        super(who);
        this.id = id;
        this.status = resourcePackStatus;
    }

    /**
     * Gets the unique ID of this pack.
     *
     * @return unique resource pack ID.
     */
    @NotNull
    public UUID getID() {
        return id;
    }

    /**
     * Gets the status of this pack.
     *
     * @return the current status
     */
    @NotNull
    public Status getStatus() {
        return status;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Status of the resource pack.
     */
    public enum Status {

        /**
         * The resource pack has been successfully downloaded and applied to the
         * client.
         */
        SUCCESSFULLY_LOADED,
        /**
         * The client refused to accept the resource pack.
         */
        DECLINED,
        /**
         * The client accepted the pack, but download failed.
         */
        FAILED_DOWNLOAD,
        /**
         * The client accepted the pack and is beginning a download of it.
         */
        ACCEPTED,
        /**
         * The client successfully downloaded the pack.
         */
        DOWNLOADED,
        /**
         * The pack URL was invalid.
         */
        INVALID_URL,
        /**
         * The client was unable to reload the pack.
         */
        FAILED_RELOAD,
        /**
         * The pack was discarded by the client.
         */
        DISCARDED;
    }
}
