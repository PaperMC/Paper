package io.papermc.paper.event.player;

import com.google.common.collect.ImmutableSet;
import io.papermc.paper.entity.TeleportFlag;
import java.util.Collections;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Event that is fired before attempting to teleport a player (Allows for pre-teleport handling, such as dismounting passengers if teleporting cross-worlds etc.)
 * <p>
 * After the handling of this event, the player will attempt to teleport to the given location.
 */
@NullMarked
public class PlayerPrepareTeleportEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location to;
    private final PlayerTeleportEvent.TeleportCause cause;
    private final Set<TeleportFlag> teleportFlags;

    @ApiStatus.Internal
    public PlayerPrepareTeleportEvent(final Player player, Location to) {
        super(player);
        this.to = to;
        this.cause = PlayerTeleportEvent.TeleportCause.PLUGIN;
        this.teleportFlags = Collections.emptySet();
    }

    @ApiStatus.Internal
    public PlayerPrepareTeleportEvent(final Player player, Location to, PlayerTeleportEvent.TeleportCause cause) {
        super(player);
        this.to = to;
        this.cause = cause;
        this.teleportFlags = Collections.emptySet();
    }

    @ApiStatus.Internal
    public PlayerPrepareTeleportEvent(final Player player, Location to, PlayerTeleportEvent.TeleportCause cause, Set<TeleportFlag> teleportFlags) {
        super(player);
        this.to = to;
        this.cause = cause;
        this.teleportFlags = ImmutableSet.copyOf(teleportFlags);
    }

    /**
     * Gets the location this player is teleporting to
     *
     * @return A copy of the Location the player is teleporting to
     */
    public Location getTo() {
        return this.to.clone();
    }

    /**
     * Gets the location this player is teleporting from
     *
     * @return A copy of the Location the player is moving from
     */
    public Location getFrom() {
        return this.getPlayer().getLocation();
    }

    /**
     * Returns the relative teleportation flags used in this teleportation.
     * This determines which axis the player will not lose their velocity in.
     *
     * @return an immutable set of relative teleportation flags
     */
    public @Unmodifiable Set<TeleportFlag> getRelativeTeleportationFlags() {
        return this.teleportFlags;
    }

    /**
     * Gets the cause of this teleportation event
     *
     * @return Cause of the event
     */
    public PlayerTeleportEvent.TeleportCause getCause() {
        return this.cause;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
