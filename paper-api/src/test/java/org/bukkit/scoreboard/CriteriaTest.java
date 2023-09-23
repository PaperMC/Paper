package org.bukkit.scoreboard;

import static org.junit.jupiter.api.Assertions.*;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class CriteriaTest extends AbstractTestingBase {

    @Test
    public void testStatistic() {
        assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.AVIATE_ONE_CM, Material.STONE)); // Generic statistic with block
        assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.AVIATE_ONE_CM, EntityType.CREEPER)); // Generic statistic with entity type

        assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.ENTITY_KILLED_BY, Material.AMETHYST_SHARD)); // Entity statistic with material
        assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.MINE_BLOCK, Material.DIAMOND_PICKAXE)); // Block statistic with item
        assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.BREAK_ITEM, Material.WATER)); // Item statistic with block
        assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.KILL_ENTITY, Material.STONE)); // Entity statistic with Material
    }
}
