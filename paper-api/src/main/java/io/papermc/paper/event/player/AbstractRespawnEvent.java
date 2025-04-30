package io.papermc.paper.event.player;

import com.google.common.collect.ImmutableSet;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import java.util.Set;

@NullMarked
public abstract class AbstractRespawnEvent extends PlayerEvent {

    protected Location respawnLocation;
    private final boolean isBedSpawn;
    private final boolean isAnchorSpawn;
    private final boolean missingRespawnBlock;
    private final PlayerRespawnEvent.RespawnReason respawnReason;
    private final Set<PlayerRespawnEvent.RespawnFlag> respawnFlags;

    protected AbstractRespawnEvent(
        final Player respawnPlayer, final Location respawnLocation, final boolean isBedSpawn,
        final boolean isAnchorSpawn, final boolean missingRespawnBlock, final PlayerRespawnEvent.RespawnReason respawnReason
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

    /**
     * Gets the current respawn location.
     *
     * @return the current respawn location
     */
    public Location getRespawnLocation() {
        return this.respawnLocation.clone();
    }

    /**
     * Gets whether the respawn location is the player's bed.
     *
     * @return {@code true} if the respawn location is the player's bed
     */
    public boolean isBedSpawn() {
        return this.isBedSpawn;
    }

    /**
     * Gets whether the respawn location is the player's respawn anchor.
     *
     * @return {@code true} if the respawn location is the player's respawn anchor
     */
    public boolean isAnchorSpawn() {
        return this.isAnchorSpawn;
    }

    /**
     * Gets whether the player is missing a valid respawn block.
     * <p>
     * This will occur if the players respawn block is obstructed,
     * or it is the first death after it was either destroyed or
     * in case of a respawn anchor, ran out of charges.
     *
     * @return whether the player is missing a valid respawn block
     */
    public boolean isMissingRespawnBlock() {
        return this.missingRespawnBlock;
    }

    /**
     * Gets the reason this respawn event was called.
     *
     * @return the reason the event was called
     */
    public PlayerRespawnEvent.RespawnReason getRespawnReason() {
        return this.respawnReason;
    }

    /**
     * Gets the set of flags that apply to this respawn.
     *
     * @return an immutable set of the flags that apply to this respawn
     * @deprecated in favour of {@link #getRespawnReason()}/{@link #isBedSpawn}/{@link #isAnchorSpawn()}
     */
    @Deprecated
    public @Unmodifiable Set<PlayerRespawnEvent.RespawnFlag> getRespawnFlags() {
        return this.respawnFlags;
    }
}
