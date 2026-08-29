package org.bukkit;

import com.google.common.collect.HashMultiset;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.stats.StatType;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.entity.EntityType;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import static org.bukkit.support.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AllFeatures
public class StatisticsAndAchievementsTest {

    @Test
    public void verifyEntityMapping() {
        for (Statistic statistic : Statistic.values()) {
            if (statistic.getType() == Statistic.Type.ENTITY) {
                for (EntityType<?> type : Registry.ENTITY_TYPE) {
                    assertNotNull(CraftStatistic.getEntityStatistic(statistic, type), statistic + " missing for " + type);
                }
            }
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void verifyStatisticMapping() {
        HashMultiset<Statistic> statistics = HashMultiset.create();
        for (StatType wrapper : BuiltInRegistries.STAT_TYPE) {
            for (Object child : wrapper.getRegistry()) {
                net.minecraft.stats.Stat<?> statistic = wrapper.get(child);
                String message = String.format("org.bukkit.Statistic is missing: '%s'", statistic);

                Statistic subject = CraftStatistic.getBukkitStatistic(statistic);
                assertThat(subject, is(not(nullValue())), message);

                if (wrapper.getRegistry() == BuiltInRegistries.BLOCK || wrapper.getRegistry() == BuiltInRegistries.ITEM) {
                    assertNotNull(CraftStatistic.getMaterialFromStatistic(statistic), "Material type map missing for " + wrapper.getRegistry().getKey(child));
                } else if (wrapper.getRegistry() == BuiltInRegistries.ENTITY_TYPE) {
                    assertNotNull(CraftStatistic.getEntityTypeFromStatistic((net.minecraft.stats.Stat<net.minecraft.world.entity.EntityType<?>>) statistic), "Entity type map missing for " + net.minecraft.world.entity.EntityType.getKey((net.minecraft.world.entity.EntityType<?>) child));
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
