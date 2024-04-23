package org.bukkit.block.spawner;

import com.google.common.base.Preconditions;
import java.util.Map;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a weighted spawn potential that can be added to a monster spawner.
 */
public class SpawnerEntry {

    private EntitySnapshot snapshot;
    private int spawnWeight;
    private SpawnRule spawnRule;
    private Equipment equipment;

    public SpawnerEntry(@NotNull EntitySnapshot snapshot, int spawnWeight, @Nullable SpawnRule spawnRule) {
        this(snapshot, spawnWeight, spawnRule, null);
    }

    public SpawnerEntry(@NotNull EntitySnapshot snapshot, int spawnWeight, @Nullable SpawnRule spawnRule, @Nullable Equipment equipment) {
        Preconditions.checkArgument(snapshot != null, "Snapshot cannot be null");

        this.snapshot = snapshot;
        this.spawnWeight = spawnWeight;
        this.spawnRule = spawnRule;
        this.equipment = equipment;
    }

    /**
     * Gets the {@link EntitySnapshot} for this SpawnerEntry.
     *
     * @return the snapshot
     */
    @NotNull
    public EntitySnapshot getSnapshot() {
        return snapshot;
    }

    /**
     * Sets the {@link EntitySnapshot} for this SpawnerEntry.
     *
     * @param snapshot the snapshot
     */
    public void setSnapshot(@NotNull EntitySnapshot snapshot) {
        Preconditions.checkArgument(snapshot != null, "Snapshot cannot be null");
        this.snapshot = snapshot;
    }

    /**
     * Gets the weight for this SpawnerEntry, when added to a spawner entries
     * with higher weight will spawn more often.
     *
     * @return the weight
     */
    public int getSpawnWeight() {
        return spawnWeight;
    }

    /**
     * Sets the weight for this SpawnerEntry, when added to a spawner entries
     * with higher weight will spawn more often.
     *
     * @param spawnWeight the new spawn weight
     */
    public void setSpawnWeight(int spawnWeight) {
        this.spawnWeight = spawnWeight;
    }

    /**
     * Gets a copy of the {@link SpawnRule} for this SpawnerEntry, or null if
     * none has been set.
     *
     * @return a copy of the spawn rule or null
     */
    @Nullable
    public SpawnRule getSpawnRule() {
        return spawnRule == null ? null : spawnRule.clone();
    }

    /**
     * Sets the {@link SpawnRule} for this SpawnerEntry, null may be used to
     * clear the current spawn rule.
     *
     * @param spawnRule the new spawn rule to use or null
     */
    public void setSpawnRule(@Nullable SpawnRule spawnRule) {
        this.spawnRule = spawnRule;
    }

    /**
     * Gets the equipment which will be applied to the spawned entity.
     *
     * @return the equipment, or null
     */
    @Nullable
    public Equipment getEquipment() {
        return equipment;
    }

    /**
     * Sets the equipment which will be applied to the spawned entity.
     *
     * @param equipment new equipment, or null
     */
    public void setEquipment(@Nullable Equipment equipment) {
        this.equipment = equipment;
    }

    /**
     * Represents the equipment loot table applied to a spawned entity.
     */
    public static class Equipment {

        private LootTable equipmentLootTable;
        private final Map<EquipmentSlot, Float> dropChances;

        public Equipment(@NotNull LootTable equipmentLootTable, @NotNull Map<EquipmentSlot, Float> dropChances) {
            this.equipmentLootTable = equipmentLootTable;
            this.dropChances = dropChances;
        }

        /**
         * Set the loot table for the entity.
         * <br>
         * To remove a loot table use null. Do not use {@link LootTables#EMPTY}
         * to clear a LootTable.
         *
         * @param table this {@link org.bukkit.entity.Mob} will have.
         */
        public void setEquipmentLootTable(@NotNull LootTable table) {
            this.equipmentLootTable = table;
        }

        /**
         * Gets the loot table for the entity.
         * <br>
         *
         * If an entity does not have a loot table, this will return null, NOT
         * an empty loot table.
         *
         * @return the loot table for this entity.
         */
        @NotNull
        public LootTable getEquipmentLootTable() {
            return this.equipmentLootTable;
        }

        /**
         * Gets a mutable map of the drop chances for each slot of the entity.
         * If non-null, the entity's drop chances will be overridden with the
         * given value.
         *
         * @return mutable map of drop chances
         */
        @NotNull
        public Map<EquipmentSlot, Float> getDropChances() {
            return this.dropChances;
        }
    }
}
