package org.bukkit.craftbukkit.event.raid;

import java.util.Collections;
import java.util.List;
import org.bukkit.Raid;
import org.bukkit.World;
import org.bukkit.entity.Raider;
import org.bukkit.event.HandlerList;
import org.bukkit.event.raid.RaidSpawnWaveEvent;
import org.jetbrains.annotations.Unmodifiable;

public class CraftRaidSpawnWaveEvent extends CraftRaidEvent implements RaidSpawnWaveEvent {

    private final List<Raider> raiders;
    private final Raider leader;

    public CraftRaidSpawnWaveEvent(final Raid raid, final World world, final Raider leader, final List<Raider> raiders) {
        super(raid, world);
        this.raiders = raiders;
        this.leader = leader;
    }

    @Override
    public @Unmodifiable List<Raider> getRaiders() {
        return Collections.unmodifiableList(this.raiders);
    }

    @Override
    public Raider getPatrolLeader() {
        return this.leader;
    }

    @Override
    public HandlerList getHandlers() {
        return RaidSpawnWaveEvent.getHandlerList();
    }
}
