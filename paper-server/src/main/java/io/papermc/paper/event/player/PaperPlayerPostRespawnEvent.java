package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class PaperPlayerPostRespawnEvent extends PaperAbstractRespawnEvent implements PlayerPostRespawnEvent {

    public PaperPlayerPostRespawnEvent(
        final Player respawnPlayer, final Location respawnLocation,
        final boolean isBedSpawn, final boolean isAnchorSpawn, final boolean missingRespawnBlock,
        final PlayerRespawnEvent.RespawnReason respawnReason
    ) {
        super(respawnPlayer, respawnLocation, isBedSpawn, isAnchorSpawn, missingRespawnBlock, respawnReason);
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerPostRespawnEvent.getHandlerList();
    }
}
