package org.bukkit.block.spawner;

import com.google.common.base.Preconditions;
import java.util.Map;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.loot.LootTable;
import org.jspecify.annotations.Nullable;

/**
 * Represents a weighted spawn potential that can be added to a monster spawner.
 */
public class SpawnerEntry {

    private EntitySnapshot snapshot;
    private int spawnWeight;
    private @Nullable SpawnRule spawnRule;
    private @Nullable Equipment equipment;

    /**
     * Creates a new spawner entry without equipment.
     *
     * @param snapshot    the entity to spawn
     * @param spawnWeight the weight of this entry
     * @param spawnRule   the rule that controls where the entity can spawn,
     *                    or null to use the default spawn rules
     */
    public SpawnerEntry(final EntitySnapshot snapshot, final int spawnWeight, final @Nullable SpawnRule spawnRule) {
        this(snapshot, spawnWeight, spawnRule, null);
    }

    /**
     * Creates a new spawner entry.
     *
     * @param snapshot    the entity to spawn
     * @param spawnWeight the weight of this entry
     * @param spawnRule   the rule that controls where the entity can spawn,
     *                    or null to use the default spawn rules
     * @param equipment   the equipment loot table for the spawned entity, or
     *                    null to not apply one
     */
    public SpawnerEntry(final EntitySnapshot snapshot, final int spawnWeight, final @Nullable SpawnRule spawnRule, final @Nullable Equipment equipment) {
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
    public EntitySnapshot getSnapshot() {
        return this.snapshot;
    }

    /**
     * Sets the {@link EntitySnapshot} for this SpawnerEntry.
     *
     * @param snapshot the snapshot
     */
    public void setSnapshot(final EntitySnapshot snapshot) {
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
        return this.spawnWeight;
    }

    /**
     * Sets the weight for this SpawnerEntry, when added to a spawner entries
     * with higher weight will spawn more often.
     *
     * @param spawnWeight the new spawn weight
     */
    public void setSpawnWeight(final int spawnWeight) {
        this.spawnWeight = spawnWeight;
    }

    /**
     * Gets a copy of the {@link SpawnRule} for this SpawnerEntry, or null if
     * none has been set.
     *
     * @return a copy of the spawn rule or null
     */
    public @Nullable SpawnRule getSpawnRule() {
        return this.spawnRule == null ? null : this.spawnRule.clone();
    }

    /**
     * Sets the {@link SpawnRule} for this SpawnerEntry, null may be used to
     * clear the current spawn rule.
     *
     * @param spawnRule the new spawn rule to use or null
     */
    public void setSpawnRule(final @Nullable SpawnRule spawnRule) {
        this.spawnRule = spawnRule;
    }

    /**
     * Gets the equipment which will be applied to the spawned entity.
     *
     * @return the equipment, or null
     */
    public @Nullable Equipment getEquipment() {
        return this.equipment;
    }

    /**
     * Sets the equipment which will be applied to the spawned entity.
     *
     * @param equipment new equipment, or null
     */
    public void setEquipment(final @Nullable Equipment equipment) {
        this.equipment = equipment;
    }

    /**
     * Represents the equipment loot table applied to a spawned entity.
     */
    public static class Equipment {

        private LootTable equipmentLootTable;
        private final Map<EquipmentSlot, Float> dropChances;

        /**
         * Creates a new equipment loot table.
         *
         * @param equipmentLootTable the loot table for the entity's equipment slots
         * @param dropChances        the drop chances for each slot of the entity
         */
        public Equipment(final LootTable equipmentLootTable, final Map<EquipmentSlot, Float> dropChances) {
            Preconditions.checkArgument(equipmentLootTable != null, "table cannot be null"); // Paper
            this.equipmentLootTable = equipmentLootTable;
            this.dropChances = dropChances;
        }

        /**
         * Set the loot table for the spawned entity's equipment slots.
         * <br>
         * To remove a loot table use the empty loot table.
         *
         * @param table this {@link org.bukkit.entity.Mob} will have.
         */
        public void setEquipmentLootTable(final LootTable table) {
            Preconditions.checkArgument(table != null, "table cannot be null"); // Paper
            this.equipmentLootTable = table;
        }

        /**
         * Gets the loot table for the spawned entity's equipment.
         * <br>
         * <p>
         * If an entity does not have a loot table, this will return an
         * empty loot table.
         *
         * @return the loot table for this entity.
         */
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
        public Map<EquipmentSlot, Float> getDropChances() {
            return this.dropChances;
        }
    }
}
