package io.papermc.paper.event.player;

import io.papermc.paper.entity.TeleportFlag;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import java.util.Set;

/**
 * Event that is fired before attempting to teleport the player (Allows for pre-teleport handling, such as dismounting passengers if teleporting cross-worlds etc.)
 * After the handling of this event, the player teleport will either proceed or cancel.
 */
@NullMarked
public class PlayerPreTeleportEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Location to;
    private PlayerTeleportEvent.TeleportCause cause;
    private Set<TeleportFlag> allFlags;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerPreTeleportEvent(@NotNull Player player, @NotNull Location to, @NotNull PlayerTeleportEvent.TeleportCause cause, @NotNull Set<TeleportFlag> allFlags) {
        super(player);
        this.to = to;
        this.cause = cause;
        this.allFlags = allFlags;
    }

    /**
     * Updates the destination location of the teleport
     */
    public void setTo(Location location) {
        this.to = location;
    }

    /**
     * The current location of the player (pre teleporting)
     * @return the current location of the player
     */
    public Location getFrom() {
        return getPlayer().getLocation();
    }

    /**
     * The location the player will be teleported to (if this event is not cancelled) {@link Location}
     * @return the destination location the player will be teleported to
     */
    public Location getTo() {
        return this.to;
    }

    /**
     * The cause of the teleportation {@link PlayerTeleportEvent.TeleportCause}
     * @return the cause for the teleportation (What causes this to trigger)
     */
    public PlayerTeleportEvent.TeleportCause getCause() {
        return this.cause;
    }

    /**
     * The teleport flags associated with this teleport {@link TeleportFlag}
     * @return associated teleport flags
     */
    public Set<TeleportFlag> getFlags() {
        return this.allFlags;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
