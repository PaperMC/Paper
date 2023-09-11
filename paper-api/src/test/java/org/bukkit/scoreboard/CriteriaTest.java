package org.bukkit.scoreboard;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.Test;

public class CriteriaTest extends AbstractTestingBase {

    @Test
    public void testStatistic() {
        Assert.assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.AVIATE_ONE_CM, Material.STONE)); // Generic statistic with block
        Assert.assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.AVIATE_ONE_CM, EntityType.CREEPER)); // Generic statistic with entity type

        Assert.assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.ENTITY_KILLED_BY, Material.AMETHYST_SHARD)); // Entity statistic with material
        Assert.assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.MINE_BLOCK, Material.DIAMOND_PICKAXE)); // Block statistic with item
        Assert.assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.BREAK_ITEM, Material.WATER)); // Item statistic with block
        Assert.assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.KILL_ENTITY, Material.STONE)); // Entity statistic with Material
    }
}
