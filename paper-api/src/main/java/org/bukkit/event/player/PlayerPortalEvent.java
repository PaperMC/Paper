package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player is about to teleport because it is in contact with a
 * portal.
 * <p>
 * For other entities see {@link org.bukkit.event.entity.EntityPortalEvent}
 */
public class PlayerPortalEvent extends PlayerTeleportEvent {
    private static final HandlerList handlers = new HandlerList();

    public PlayerPortalEvent(@NotNull final Player player, @NotNull final Location from, @Nullable final Location to) {
        super(player, from, to);
    }

    public PlayerPortalEvent(@NotNull Player player, @NotNull Location from, @Nullable Location to, @NotNull TeleportCause cause) {
        super(player, from, to, cause);
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
