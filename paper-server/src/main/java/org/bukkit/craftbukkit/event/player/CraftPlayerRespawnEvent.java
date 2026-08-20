package org.bukkit.craftbukkit.event.player;

import com.google.common.base.Preconditions;
import io.papermc.paper.event.player.PaperAbstractRespawnEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRespawnEvent;

public class CraftPlayerRespawnEvent extends PaperAbstractRespawnEvent implements PlayerRespawnEvent {

    public CraftPlayerRespawnEvent(
        final Player respawnPlayer, final Location respawnLocation,
        final boolean isBedSpawn, final boolean isAnchorSpawn, final boolean missingRespawnBlock,
        final RespawnReason respawnReason
    ) {
        super(respawnPlayer, respawnLocation, isBedSpawn, isAnchorSpawn, missingRespawnBlock, respawnReason);
    }

    @Override
    public void setRespawnLocation(Location respawnLocation) {
        Preconditions.checkArgument(respawnLocation != null, "Respawn location can not be null");
        Preconditions.checkArgument(respawnLocation.getWorld() != null, "Respawn world can not be null");

        this.respawnLocation = respawnLocation.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerRespawnEvent.getHandlerList();
    }
}
