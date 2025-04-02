package org.bukkit.craftbukkit.spawner;

import com.google.common.base.Preconditions;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.spawner.Spawner;

/**
 * A common parent interface for both the {@link org.bukkit.craftbukkit.block.CraftCreatureSpawner} and minecart mob spawner.
 */
public interface PaperSharedSpawnerLogic extends Spawner {

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
        final CompoundTag entity = new CompoundTag();
        entity.putString(Entity.ID_TAG, BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ITEM).toString());
        entity.put("Item", item.save(this.getInternalWorld().registryAccess()));

        this.setNextSpawnData(
            new net.minecraft.world.level.SpawnData(
                entity,
                java.util.Optional.empty(),
                Optional.ofNullable(this.getSpawner().nextSpawnData).flatMap(SpawnData::equipment)
            )
        );
    }
}
