package org.bukkit.scoreboard;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@VanillaFeature
public class CriteriaTest {

    @Test
    public void testStatistic() {
        Material item = mock();
        when(item.isItem()).thenReturn(true);
        when(item.isBlock()).thenReturn(false);

        Material block = mock();
        when(block.isItem()).thenReturn(false);
        when(block.isBlock()).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.AVIATE_ONE_CM, Material.STONE)); // Generic statistic with block
        assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.AVIATE_ONE_CM, EntityType.CREEPER)); // Generic statistic with entity type

        assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.ENTITY_KILLED_BY, Material.AMETHYST_SHARD)); // Entity statistic with material
        assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.MINE_BLOCK, item)); // Block statistic with item
        assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.BREAK_ITEM, block)); // Item statistic with block
        assertThrows(IllegalArgumentException.class, () -> Criteria.statistic(Statistic.KILL_ENTITY, Material.STONE)); // Entity statistic with Material
    }
}
