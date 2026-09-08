package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import java.util.Set;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import org.bukkit.block.EndGateway;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.event.player.CraftPlayerTeleportEvent;

public class PaperPlayerTeleportEndGatewayEvent extends CraftPlayerTeleportEvent implements PlayerTeleportEndGatewayEvent {

    private final EndGateway gateway;

    public PaperPlayerTeleportEndGatewayEvent(final ServerPlayer player, final Level newLevel, PositionMoveRotation newPosition, final TheEndGatewayBlockEntity gateway) {
        super(player, newLevel, newPosition, TeleportCause.END_GATEWAY, Set.of());
        this.gateway = (EndGateway) CraftBlockStates.snapshotOf(gateway);
    }

    @Override
    public EndGateway getGateway() {
        return this.gateway;
    }
}
