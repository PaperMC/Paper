package org.bukkit.event.raid;

import org.bukkit.Raid;
import org.bukkit.World;
import org.bukkit.event.world.WorldEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents events related to raids.
 */
public abstract class RaidEvent extends WorldEvent {

    private final Raid raid;

    protected RaidEvent(@NotNull Raid raid, @NotNull World world) {
        super(world);
        this.raid = raid;
    }

    /**
     * Returns the raid involved with this event.
     *
     * @return Raid
     */
    @NotNull
    public Raid getRaid() {
        return raid;
    }
}
