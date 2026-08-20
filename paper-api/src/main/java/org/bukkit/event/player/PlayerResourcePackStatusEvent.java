package org.bukkit.event.player;

import java.util.UUID;
import net.kyori.adventure.resource.ResourcePackRequest;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * Called when a player takes action on a resource pack request.
 *
 * @see Player#setResourcePack(String, String)
 * @see Player#setResourcePack(String, String, boolean)
 * @see Player#sendResourcePacks(ResourcePackRequest)
 * @see Player#addResourcePack(UUID, String, byte[], String, boolean)
 */
public interface PlayerResourcePackStatusEvent extends PlayerEvent {

    /**
     * Gets the unique ID of this pack.
     *
     * @return unique resource pack ID.
     */
    UUID getID();

    /**
     * Gets the status of this pack.
     *
     * @return the current status
     */
    Status getStatus();

    /**
     * @deprecated This is no longer sent from the client and will always be null
     */
    @Contract("-> null")
    @Deprecated(forRemoval = true)
    default @Nullable String getHash() {
        return null;
    }

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * Status of the resource pack.
     */
    enum Status {

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
