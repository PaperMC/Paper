package io.papermc.paper.event.player;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.Unmodifiable;

public abstract class PaperAbstractRespawnEvent extends CraftPlayerEvent implements AbstractRespawnEvent {

    protected Location respawnLocation;
    private final boolean isBedSpawn;
    private final boolean isAnchorSpawn;
    private final boolean missingRespawnBlock;
    private final PlayerRespawnEvent.RespawnReason respawnReason;
    private final Set<PlayerRespawnEvent.RespawnFlag> respawnFlags;

    protected PaperAbstractRespawnEvent(
        final Player respawnPlayer, final Location respawnLocation,
        final boolean isBedSpawn, final boolean isAnchorSpawn, final boolean missingRespawnBlock,
        final PlayerRespawnEvent.RespawnReason respawnReason
    ) {
        super(respawnPlayer);
        this.respawnLocation = respawnLocation;
        this.isBedSpawn = isBedSpawn;
        this.isAnchorSpawn = isAnchorSpawn;
        this.missingRespawnBlock = missingRespawnBlock;
        this.respawnReason = respawnReason;
        ImmutableSet.Builder<PlayerRespawnEvent.RespawnFlag> builder = ImmutableSet.builder();
        if (respawnReason == PlayerRespawnEvent.RespawnReason.END_PORTAL) builder.add(PlayerRespawnEvent.RespawnFlag.END_PORTAL);
        if (this.isBedSpawn) builder.add(PlayerRespawnEvent.RespawnFlag.BED_SPAWN);
        if (this.isAnchorSpawn) builder.add(PlayerRespawnEvent.RespawnFlag.ANCHOR_SPAWN);
        this.respawnFlags = builder.build();
    }

    @Override
    public Location getRespawnLocation() {
        return this.respawnLocation.clone();
    }

    @Override
    public boolean isBedSpawn() {
        return this.isBedSpawn;
    }

    @Override
    public boolean isAnchorSpawn() {
        return this.isAnchorSpawn;
    }

    @Override
    public boolean isMissingRespawnBlock() {
        return this.missingRespawnBlock;
    }

    @Override
    public PlayerRespawnEvent.RespawnReason getRespawnReason() {
        return this.respawnReason;
    }

    @Deprecated
    public @Unmodifiable Set<PlayerRespawnEvent.RespawnFlag> getRespawnFlags() {
        return this.respawnFlags;
    }
}
