package org.bukkit.craftbukkit.event.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import org.bukkit.block.TrialSpawner;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.TrialSpawnerSpawnEvent;

public class CraftTrialSpawnerSpawnEvent extends CraftEntitySpawnEvent implements TrialSpawnerSpawnEvent {

    private final TrialSpawner spawner;

    public CraftTrialSpawnerSpawnEvent(final Entity entity, final TrialSpawner spawner) {
        super(entity);
        this.spawner = spawner;
    }

    public CraftTrialSpawnerSpawnEvent(final net.minecraft.world.entity.Entity entity, final BlockPos pos) {
        final TrialSpawner spawner = (TrialSpawner) entity.level().getBlockEntity(pos, BlockEntityTypes.TRIAL_SPAWNER)
            .map(CraftBlockStates::snapshotOf).orElseThrow();
        this(entity.getBukkitEntity(), spawner);
    }

    @Override
    public TrialSpawner getTrialSpawner() {
        return this.spawner;
    }
}
