package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.block.EndGateway;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperPlayerTeleportEndGatewayEvent extends PlayerTeleportEndGatewayEvent {

    @ApiStatus.Internal
    public PaperPlayerTeleportEndGatewayEvent(final Player player, final Location from, final Location to, final EndGateway gateway) {
        super(player, from, to, gateway);
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
