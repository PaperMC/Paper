package org.bukkit.craftbukkit.spawner;

import com.google.common.base.Preconditions;
import java.util.Optional;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.storage.TagValueOutput;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.spawner.Spawner;
import org.slf4j.Logger;

/**
 * A common parent interface for both the {@link org.bukkit.craftbukkit.block.CraftCreatureSpawner} and minecart mob spawner.
 */
public interface PaperSharedSpawnerLogic extends Spawner {

    static final Logger LOGGER = LogUtils.getLogger();

    BaseSpawner getSpawner();

    Level getInternalWorld();

    BlockPos getInternalPosition();
    default boolean isActivated() {
        return this.getSpawner().isNearPlayer(this.getInternalWorld(), this.getInternalPosition());
    }

    default void resetTimer() {
        this.getSpawner().delay(this.getInternalWorld(), this.getInternalPosition());
    }

    default void setNextSpawnData(SpawnData spawnData) {
        this.getSpawner().setNextSpawnData(this.getInternalWorld(), this.getInternalPosition(), spawnData);
    }

    default void setSpawnedItem(final ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null && !itemStack.getType().isAir(), "spawners cannot spawn air");

        final net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(itemStack);

        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(() -> getSpawner().toString(), LOGGER)) {
            TagValueOutput tagValueOutput = TagValueOutput.createWithContext(scopedCollector, this.getInternalWorld().registryAccess());
            tagValueOutput.putString(Entity.TAG_ID, BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ITEM).toString());
            tagValueOutput.store("Item", net.minecraft.world.item.ItemStack.CODEC, item);

            this.setNextSpawnData(
                new net.minecraft.world.level.SpawnData(
                    tagValueOutput.buildResult(),
                    java.util.Optional.empty(),
                    Optional.ofNullable(this.getSpawner().nextSpawnData).flatMap(SpawnData::equipment)
                )
            );
        }

    }
}
