package org.bukkit.event.player;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player takes action on a resource pack request.
 *
 * @see Player#setResourcePack(String, String)
 * @see Player#setResourcePack(String, String, boolean)
 */
public class PlayerResourcePackStatusEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final UUID id;
    private final Status status;

    @ApiStatus.Internal
    public PlayerResourcePackStatusEvent(@NotNull final Player player, @NotNull UUID id, @NotNull Status resourcePackStatus) {
        super(player);
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
        return this.id;
    }

    /**
     * Gets the status of this pack.
     *
     * @return the current status
     */
    @NotNull
    public Status getStatus() {
        return this.status;
    }

    /**
     * @deprecated This is no longer sent from the client and will always be null
     */
    @Deprecated(forRemoval = true)
    @Contract("-> null")
    public String getHash() {
        return null;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
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
        DISCARDED
    }
}
