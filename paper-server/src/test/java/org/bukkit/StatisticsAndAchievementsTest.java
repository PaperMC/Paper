package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import com.google.common.collect.HashMultiset;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.stats.StatisticWrapper;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.entity.EntityType;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class StatisticsAndAchievementsTest extends AbstractTestingBase {

    @Test
    @SuppressWarnings("unchecked")
    public void verifyEntityMapping() throws Throwable {
        for (Statistic statistic : Statistic.values()) {
            if (statistic.getType() == Statistic.Type.ENTITY) {
                for (EntityType entity : EntityType.values()) {
                    if (entity.getName() != null) {
                        assertNotNull(CraftStatistic.getEntityStatistic(statistic, entity), statistic + " missing for " + entity);
                    }
                }
            }
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void verifyStatisticMapping() throws Throwable {
        HashMultiset<Statistic> statistics = HashMultiset.create();
        for (StatisticWrapper wrapper : BuiltInRegistries.STAT_TYPE) {
            for (Object child : wrapper.getRegistry()) {
                net.minecraft.stats.Statistic<?> statistic = wrapper.get(child);
                String message = String.format("org.bukkit.Statistic is missing: '%s'", statistic);

                Statistic subject = CraftStatistic.getBukkitStatistic(statistic);
                assertThat(subject, is(not(nullValue())), message);

                if (wrapper.getRegistry() == BuiltInRegistries.BLOCK || wrapper.getRegistry() == BuiltInRegistries.ITEM) {
                    assertNotNull(CraftStatistic.getMaterialFromStatistic(statistic), "Material type map missing for " + wrapper.getRegistry().getKey(child));
                } else if (wrapper.getRegistry() == BuiltInRegistries.ENTITY_TYPE) {
                    assertNotNull(CraftStatistic.getEntityTypeFromStatistic((net.minecraft.stats.Statistic<EntityTypes<?>>) statistic), "Entity type map missing for " + EntityTypes.getKey((EntityTypes<?>) child));
                }

                statistics.add(subject);
            }
        }

        for (Statistic statistic : Statistic.values()) {
            String message = String.format("org.bukkit.Statistic.%s does not have a corresponding minecraft statistic", statistic.name());
            assertThat(statistics.remove(statistic, statistics.count(statistic)), is(greaterThan(0)), message);
        }
    }
}
