package org.bukkit.craftbukkit.event.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.jspecify.annotations.Nullable;

public class CraftSpawnerSpawnEvent extends CraftEntitySpawnEvent implements SpawnerSpawnEvent {

    private final @Nullable CreatureSpawner spawner;

    public CraftSpawnerSpawnEvent(final Entity entity, final @Nullable CreatureSpawner spawner) {
        super(entity);
        this.spawner = spawner;
    }

    public CraftSpawnerSpawnEvent(final net.minecraft.world.entity.Entity entity, final BlockPos pos) {
        final CreatureSpawner spawner = (CreatureSpawner) entity.level().getBlockEntity(pos, BlockEntityTypes.MOB_SPAWNER)
            .map(CraftBlockStates::snapshotOf).orElse(null);
        this(entity.getBukkitEntity(), spawner);
    }

    @Override
    public @Nullable CreatureSpawner getSpawner() {
        return this.spawner;
    }
}
