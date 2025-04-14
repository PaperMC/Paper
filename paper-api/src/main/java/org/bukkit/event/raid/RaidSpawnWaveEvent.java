package org.bukkit.event.raid;

import java.util.Collections;
import java.util.List;
import org.bukkit.Raid;
import org.bukkit.World;
import org.bukkit.entity.Raider;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Called when a raid wave spawns.
 */
public class RaidSpawnWaveEvent extends RaidEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final List<Raider> raiders;
    private final Raider leader;

    @ApiStatus.Internal
    public RaidSpawnWaveEvent(@NotNull Raid raid, @NotNull World world, @NotNull Raider leader, @NotNull List<Raider> raiders) {
        super(raid, world);
        this.raiders = raiders;
        this.leader = leader;
    }

    /**
     * Returns all {@link Raider} that spawned in this wave.
     *
     * @return an immutable list of raiders
     */
    @NotNull
    public @Unmodifiable List<Raider> getRaiders() {
        return Collections.unmodifiableList(this.raiders);
    }

    /**
     * Returns the patrol leader.
     *
     * @return {@link Raider}
     */
    @NotNull
    public Raider getPatrolLeader() {
        return this.leader;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
