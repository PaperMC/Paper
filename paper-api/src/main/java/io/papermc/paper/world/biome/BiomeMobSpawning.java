package io.papermc.paper.world.biome;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.SpawnCategory;
import org.checkerframework.checker.index.qual.Positive;
import java.util.List;
import java.util.Map;

public interface BiomeMobSpawning {

    /**
     * Spawn category to a list of spawner data objects, one for each
     * mob which should spawn in this biome. If this object doesn't
     * contain a certain category, mobs in this category do not spawn.
     *
     * @return the spawner data
     */
    Map<SpawnCategory, List<MobData>> spawners();

    /**
     * New mob's cost. Only mobs listed here use the spawn cost mechanism.
     *
     * @see <a href="https://minecraft.wiki/w/Mob_spawning#Spawn_costs">Mob spawning#Spawn costs</a>
     * @return the map of entity type to cost
     */
    Map<EntityType, SpawnCost> entityCost();

    /**
     * The spawner data for a single mob.
     */
    interface MobData {

        /**
         * The entity type of the mob.
         *
         * @return the entity type
         */
        EntityType type();

        /**
         * The weight of the mob. Higher weights mean the mob is more likely to spawn.
         *
         * @return the weight
         */
        int weight();

        /**
         * The minimum count of mobs to spawn in a pack.
         *
         * @return the minimum count
         */
        @Positive
        int minCount();

        /**
         * The maximum count of mobs to spawn in a pack.
         *
         * @return the maximum count
         */
        @Positive
        int maxCount();
    }

    /**
     * The spawn cost for a mob.
     *
     * @see <a href="https://minecraft.wiki/w/Mob_spawning#Spawn_costs">Mob spawning#Spawn costs</a>
     */
    interface SpawnCost {

        /**
         * The energy budget of the mob.
         *
         * @return the energy budget
         */
        double energyBudget();

        /**
         * The charge of the mob.
         *
         * @return the charge
         */
        double charge();
    }
}
