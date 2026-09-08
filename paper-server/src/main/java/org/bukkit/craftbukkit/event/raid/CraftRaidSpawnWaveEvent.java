package org.bukkit.craftbukkit.event.raid;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.world.level.Level;
import org.bukkit.Raid;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftRaid;
import org.bukkit.entity.Raider;
import org.bukkit.event.HandlerList;
import org.bukkit.event.raid.RaidSpawnWaveEvent;
import org.jetbrains.annotations.Unmodifiable;

public class CraftRaidSpawnWaveEvent extends CraftRaidEvent implements RaidSpawnWaveEvent {

    private final List<Raider> raiders;
    private final Raider leader;

    public CraftRaidSpawnWaveEvent(final Raid raid, final World world, final Raider leader, final List<Raider> raiders) {
        super(raid, world);
        this.raiders = Collections.unmodifiableList(raiders);
        this.leader = leader;
    }

    public CraftRaidSpawnWaveEvent(final Level level, final net.minecraft.world.entity.raid.Raid raid, final net.minecraft.world.entity.raid.Raider leader, final Set<net.minecraft.world.entity.raid.Raider> raiders) {
        this(
            new CraftRaid(raid, level),
            level.getWorld(),
            (Raider) leader.getBukkitEntity(),
            raiders.stream().map(r -> (Raider) r.getBukkitEntity()).toList()
        );
    }

    @Override
    public @Unmodifiable List<Raider> getRaiders() {
        return this.raiders;
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
