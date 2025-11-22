package io.papermc.paper.event.player;

import com.google.common.base.Preconditions;
import io.papermc.paper.entity.TeleportFlag;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.Set;

@NullMarked
public class PaperPlayerTeleportEvent extends PlayerTeleportEvent {

    @ApiStatus.Internal
    public PaperPlayerTeleportEvent(final Player player, final Location from, final @Nullable Location to) {
        super(player, from, to);
    }

    @ApiStatus.Internal
    public PaperPlayerTeleportEvent(final Player player, final Location from, final @Nullable Location to, final TeleportCause cause) {
        super(player, from, to, cause);
    }

    @ApiStatus.Internal
    public PaperPlayerTeleportEvent(final Player player, final Location from, final @Nullable Location to, final TeleportCause cause, final Set<TeleportFlag.Relative> teleportFlags) {
        super(player, from, to, cause, teleportFlags);
    }

    @Override
    public void setTo(final Location to) {
        Preconditions.checkArgument(CraftLocation.isInSpawnableBounds(to), "destination Location is not in spawnable bounds");
        super.setTo(to);
    }

    @Override
    public void setFrom(final Location from) {
        Preconditions.checkArgument(CraftLocation.isInSpawnableBounds(from), "origin Location is not in spawnable bounds");
        super.setFrom(from);
    }
}
