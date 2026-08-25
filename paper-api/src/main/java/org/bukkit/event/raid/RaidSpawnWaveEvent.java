package org.bukkit.event.raid;

import java.util.List;
import org.bukkit.entity.Raider;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Called when a raid wave spawns.
 */
public interface RaidSpawnWaveEvent extends RaidEvent {

    /**
     * Returns all {@link Raider} that spawned in this wave.
     *
     * @return an immutable list of raiders
     */
    @Unmodifiable List<Raider> getRaiders();

    /**
     * Returns the patrol leader.
     *
     * @return {@link Raider}
     */
    Raider getPatrolLeader();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
