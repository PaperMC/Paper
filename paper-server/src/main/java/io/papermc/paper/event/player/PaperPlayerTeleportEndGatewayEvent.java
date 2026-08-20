package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import org.bukkit.Location;
import org.bukkit.block.EndGateway;
import org.bukkit.craftbukkit.event.player.CraftPlayerTeleportEvent;
import org.bukkit.entity.Player;

public class PaperPlayerTeleportEndGatewayEvent extends CraftPlayerTeleportEvent implements PlayerTeleportEndGatewayEvent {

    private final EndGateway gateway;

    public PaperPlayerTeleportEndGatewayEvent(final Player player, final Location from, final Location to, final EndGateway gateway) {
        super(player, from, to, TeleportCause.END_GATEWAY);
        this.gateway = gateway;
    }

    @Override
    public EndGateway getGateway() {
        return this.gateway;
    }
}
