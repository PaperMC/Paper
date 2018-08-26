package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import net.minecraft.server.EntityTypes;
import net.minecraft.server.IRegistry;
import net.minecraft.server.StatisticWrapper;

import org.bukkit.entity.EntityType;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

import com.google.common.collect.HashMultiset;

public class StatisticsAndAchievementsTest extends AbstractTestingBase {

    @Test
    @SuppressWarnings("unchecked")
    public void verifyEntityMapping() throws Throwable {
        for (Statistic statistic : Statistic.values()) {
            if (statistic.getType() == Statistic.Type.ENTITY) {
                for (EntityType entity : EntityType.values()) {
                    if (entity.getName() != null) {
                        assertNotNull(statistic + " missing for " + entity, CraftStatistic.getEntityStatistic(statistic, entity));
                    }
                }
            }
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void verifyStatisticMapping() throws Throwable {
        HashMultiset<Statistic> statistics = HashMultiset.create();
        for (StatisticWrapper wrapper : (Iterable<StatisticWrapper<?>>) IRegistry.STATS) { // Eclipse fail
            for (Object child : wrapper.a()) {
                net.minecraft.server.Statistic<?> statistic = wrapper.b(child);
                String message = String.format("org.bukkit.Statistic is missing: '%s'", statistic);

                Statistic subject = CraftStatistic.getBukkitStatistic(statistic);
                assertThat(message, subject, is(not(nullValue())));

                if (wrapper.a() == IRegistry.BLOCK || wrapper.a() == IRegistry.ITEM) {
                    assertNotNull("Material type map missing for " + child, CraftStatistic.getMaterialFromStatistic(statistic));
                } else if (wrapper.a() == IRegistry.ENTITY_TYPE) {
                    assertNotNull("Entity type map missing for " + EntityTypes.getName((EntityTypes<?>) child), CraftStatistic.getEntityTypeFromStatistic((net.minecraft.server.Statistic<EntityTypes<?>>) statistic));
                }

                statistics.add(subject);
            }
        }

        for (Statistic statistic : Statistic.values()) {
            String message = String.format("org.bukkit.Statistic.%s does not have a corresponding minecraft statistic", statistic.name());
            assertThat(message, statistics.remove(statistic, statistics.count(statistic)), is(greaterThan(0)));
        }
    }
}
