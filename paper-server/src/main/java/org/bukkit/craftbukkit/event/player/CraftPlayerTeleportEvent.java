package org.bukkit.craftbukkit.event.player;

import io.papermc.paper.entity.TeleportFlag;
import java.util.Set;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

public class CraftPlayerTeleportEvent extends CraftPlayerMoveEvent implements PlayerTeleportEvent {

    private final Set<TeleportFlag.Relative> teleportFlags;
    private final TeleportCause cause;

    public CraftPlayerTeleportEvent(final Player player, final Location from, final @Nullable Location to, final TeleportCause cause) {
        this(player, from, to, cause, Set.of());
    }

    public CraftPlayerTeleportEvent(
        final Player player, final Location from, final @Nullable Location to, final TeleportCause cause, final Set<TeleportFlag.Relative> teleportFlags
    ) {
        super(player, from, to);
        this.cause = cause;
        this.teleportFlags = teleportFlags;
    }

    public CraftPlayerTeleportEvent(
        final ServerPlayer player, final Level newLevel, final PositionMoveRotation destination, final TeleportCause cause, final Set<TeleportFlag.Relative> teleportFlags
    ) {
        final Player p = player.getBukkitEntity();
        this(p, p.getLocation(), CraftLocation.toBukkit(destination, newLevel), cause, teleportFlags);
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
