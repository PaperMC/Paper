package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player takes action on a resource pack request sent via
 * {@link Player#setResourcePack(java.lang.String)}.
 */
public class PlayerResourcePackStatusEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Status status;

    public PlayerResourcePackStatusEvent(@NotNull final Player who, @NotNull Status resourcePackStatus) {
        super(who);
        this.status = resourcePackStatus;
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
        ACCEPTED;
    }
}
