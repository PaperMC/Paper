package org.bukkit.craftbukkit;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityRaider;
import net.minecraft.server.World;
import org.bukkit.Location;
import org.bukkit.Raid;
import org.bukkit.entity.Raider;

public final class CraftRaid implements Raid {

    private final net.minecraft.server.Raid handle;

    public CraftRaid(net.minecraft.server.Raid handle) {
        this.handle = handle;
    }

    @Override
    public boolean isStarted() {
        return handle.j(); // PAIL rename isStarted
    }

    @Override
    public long getActiveTicks() {
        return handle.i;
    }

    @Override
    public int getBadOmenLevel() {
        return handle.o;
    }

    @Override
    public void setBadOmenLevel(int badOmenLevel) {
        int max = handle.l(); // PAIL rename getMaxBadOmenLevel
        Preconditions.checkArgument(0 <= badOmenLevel && badOmenLevel <= max, "Bad Omen level must be between 0 and %s", max);
        handle.o = badOmenLevel;
    }

    @Override
    public Location getLocation() {
        BlockPosition pos = handle.t(); // PAIL rename getCenterLocation
        World world = handle.i(); // PAIL rename getWorld
        return new Location(world.getWorld(), pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public RaidStatus getStatus() {
        if (handle.d()) { // PAIL rename isStopped
            return RaidStatus.STOPPED;
        } else if (handle.e()) { // PAIL rename isVictory
            return RaidStatus.VICTORY;
        } else if (handle.f()) { // PAIL rename isLoss
            return RaidStatus.LOSS;
        } else {
            return RaidStatus.ONGOING;
        }
    }

    @Override
    public int getSpawnedGroups() {
        return handle.k(); // PAIL rename countSpawnedGroups
    }

    @Override
    public int getTotalGroups() {
        return handle.v + (handle.o > 1 ? 1 : 0);
    }

    @Override
    public int getTotalWaves() {
        return handle.v;
    }

    @Override
    public float getTotalHealth() {
        return handle.q(); // PAIL rename sumMobHealth
    }

    @Override
    public Set<UUID> getHeroes() {
        return Collections.unmodifiableSet(handle.h);
    }

    @Override
    public List<Raider> getRaiders() {
        return handle.getRaiders().stream().map(new Function<EntityRaider, Raider>() {
            @Override
            public Raider apply(EntityRaider entityRaider) {
                return (Raider) entityRaider.getBukkitEntity();
            }
        }).collect(ImmutableList.toImmutableList());
    }
}
