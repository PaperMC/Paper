package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.Raid;
import org.bukkit.Raid.RaidStatus;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Raider;

public final class CraftRaid implements Raid {

    private final net.minecraft.world.entity.raid.Raid handle;

    public CraftRaid(net.minecraft.world.entity.raid.Raid handle) {
        this.handle = handle;
    }

    @Override
    public boolean isStarted() {
        return this.handle.isStarted();
    }

    @Override
    public long getActiveTicks() {
        return this.handle.ticksActive;
    }

    @Override
    public int getBadOmenLevel() {
        return this.handle.raidOmenLevel;
    }

    @Override
    public void setBadOmenLevel(int badOmenLevel) {
        int max = this.handle.getMaxRaidOmenLevel();
        Preconditions.checkArgument(0 <= badOmenLevel && badOmenLevel <= max, "Bad Omen level must be between 0 and %s", max);
        this.handle.raidOmenLevel = badOmenLevel;
    }

    @Override
    public Location getLocation() {
        BlockPos pos = this.handle.getCenter();
        Level world = this.handle.getLevel();
        return CraftLocation.toBukkit(pos, world.getWorld());
    }

    @Override
    public RaidStatus getStatus() {
        if (this.handle.isStopped()) {
            return RaidStatus.STOPPED;
        } else if (this.handle.isVictory()) {
            return RaidStatus.VICTORY;
        } else if (this.handle.isLoss()) {
            return RaidStatus.LOSS;
        } else {
            return RaidStatus.ONGOING;
        }
    }

    @Override
    public int getSpawnedGroups() {
        return this.handle.getGroupsSpawned();
    }

    @Override
    public int getTotalGroups() {
        return this.handle.numGroups + (this.handle.raidOmenLevel > 1 ? 1 : 0);
    }

    @Override
    public int getTotalWaves() {
        return this.handle.numGroups;
    }

    @Override
    public float getTotalHealth() {
        return this.handle.getHealthOfLivingRaiders();
    }

    @Override
    public Set<UUID> getHeroes() {
        return Collections.unmodifiableSet(this.handle.heroesOfTheVillage);
    }

    @Override
    public List<Raider> getRaiders() {
        return this.handle.getRaiders().stream().map(new Function<net.minecraft.world.entity.raid.Raider, Raider>() {
            @Override
            public Raider apply(net.minecraft.world.entity.raid.Raider entityRaider) {
                return (Raider) entityRaider.getBukkitEntity();
            }
        }).collect(ImmutableList.toImmutableList());
    }

    public net.minecraft.world.entity.raid.Raid getHandle() {
        return this.handle;
    }
}
