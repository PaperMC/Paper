package org.bukkit.craftbukkit.event.player;

import io.papermc.paper.entity.TeleportFlag;
import java.util.Collections;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

public class CraftPlayerTeleportEvent extends CraftPlayerMoveEvent implements PlayerTeleportEvent {

    private final Set<TeleportFlag.Relative> teleportFlags;
    private final TeleportCause cause;

    public CraftPlayerTeleportEvent(final Player player, final Location from, final @Nullable Location to, final TeleportCause cause) {
        this(player, from, to, cause, Collections.emptySet());
    }

    public CraftPlayerTeleportEvent(final Player player, final Location from, final @Nullable Location to, final TeleportCause cause, final Set<TeleportFlag.Relative> teleportFlags) {
        super(player, from, to);
        this.cause = cause;
        this.teleportFlags = teleportFlags;
    }

    @Override
    public TeleportCause getCause() {
        return this.cause;
    }

    @Override
    public @Unmodifiable Set<TeleportFlag.Relative> getRelativeTeleportationFlags() {
        return this.teleportFlags;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerTeleportEvent.getHandlerList();
    }
}
