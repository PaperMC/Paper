package io.papermc.paper.event.player;

import java.util.Set;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerEventNew;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.NonExtendable
public interface AbstractRespawnEvent extends PlayerEventNew {

    /**
     * Gets the current respawn location.
     *
     * @return the current respawn location
     */
    Location getRespawnLocation();

    /**
     * Gets whether the respawn location is the player's bed.
     *
     * @return {@code true} if the respawn location is the player's bed
     */
    boolean isBedSpawn();

    /**
     * Gets whether the respawn location is the player's respawn anchor.
     *
     * @return {@code true} if the respawn location is the player's respawn anchor
     */
    boolean isAnchorSpawn();

    /**
     * Gets whether the player is missing a valid respawn block.
     * <p>
     * This will occur if the players respawn block is obstructed,
     * or it is the first death after it was either destroyed or
     * in case of a respawn anchor, ran out of charges.
     *
     * @return whether the player is missing a valid respawn block
     */
    boolean isMissingRespawnBlock();

    /**
     * Gets the reason this respawn event was called.
     *
     * @return the reason the event was called
     */
    PlayerRespawnEvent.RespawnReason getRespawnReason();

    /**
     * Gets the set of flags that apply to this respawn.
     *
     * @return an immutable set of the flags that apply to this respawn
     * @deprecated in favour of {@link #getRespawnReason()}/{@link #isBedSpawn}/{@link #isAnchorSpawn()}
     */
    @Deprecated
    @Unmodifiable Set<PlayerRespawnEvent.RespawnFlag> getRespawnFlags();
}
