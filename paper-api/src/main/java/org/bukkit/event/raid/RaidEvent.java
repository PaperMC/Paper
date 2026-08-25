package org.bukkit.event.raid;

import org.bukkit.Raid;
import org.bukkit.event.world.WorldEvent;

/**
 * Represents events related to raids.
 */
public interface RaidEvent extends WorldEvent {

    /**
     * Returns the raid involved with this event.
     *
     * @return the raid
     */
    Raid getRaid();
}
