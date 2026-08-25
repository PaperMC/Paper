package org.bukkit.craftbukkit.event.raid;

import org.bukkit.Raid;
import org.bukkit.World;
import org.bukkit.craftbukkit.event.world.CraftWorldEvent;
import org.bukkit.event.raid.RaidEvent;

public abstract class CraftRaidEvent extends CraftWorldEvent implements RaidEvent {

    private final Raid raid;

    protected CraftRaidEvent(final Raid raid, final World world) {
        super(world);
        this.raid = raid;
    }

    @Override
    public Raid getRaid() {
        return this.raid;
    }
}
