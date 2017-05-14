package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import net.minecraft.server.StatisticList;

import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

import com.google.common.collect.HashMultiset;

public class StatisticsAndAchievementsTest extends AbstractTestingBase {

    @Test
    @SuppressWarnings("unchecked")
    public void verifyStatisticMapping() throws Throwable {
        HashMultiset<Statistic> statistics = HashMultiset.create();
        for (net.minecraft.server.Statistic statistic : (List<net.minecraft.server.Statistic>) StatisticList.stats) {
            String name = statistic.name;

            String message = String.format("org.bukkit.Statistic is missing: '%s'", name);

            Statistic subject = CraftStatistic.getBukkitStatistic(statistic);
            assertThat(message, subject, is(not(nullValue())));

            statistics.add(subject);
        }

        for (Statistic statistic : Statistic.values()) {
            String message = String.format("org.bukkit.Statistic.%s does not have a corresponding minecraft statistic", statistic.name());
            assertThat(message, statistics.remove(statistic, statistics.count(statistic)), is(greaterThan(0)));
        }
    }
}
