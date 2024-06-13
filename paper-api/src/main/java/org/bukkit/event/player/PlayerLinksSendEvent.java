package org.bukkit.event.player;

import org.bukkit.ServerLinks;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when the list of links is sent to the player.
 */
@ApiStatus.Experimental
public class PlayerLinksSendEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final ServerLinks links;

    public PlayerLinksSendEvent(@NotNull final Player player, @NotNull final ServerLinks links) {
        super(player);
        this.links = links;
    }

    /**
     * Gets the links to be sent, for modification.
     *
     * @return the links
     */
    @NotNull
    public ServerLinks getLinks() {
        return links;
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
}
