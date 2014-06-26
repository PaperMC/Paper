package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Collections;
import java.util.List;

import net.minecraft.server.AchievementList;
import net.minecraft.server.StatisticList;

import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;

public class StatisticsAndAchievementsTest extends AbstractTestingBase {
    @Test
    @SuppressWarnings("unchecked")
    public void verifyAchievementMapping() throws Throwable {
        List<Achievement> achievements = Lists.newArrayList(Achievement.values());
        for (net.minecraft.server.Achievement achievement : (List<net.minecraft.server.Achievement>) AchievementList.e) {
            String name = achievement.name;

            String message = String.format("org.bukkit.Achievement is missing: '%s'", name);

            Achievement subject = CraftStatistic.getBukkitAchievement(achievement);
            assertThat(message, subject, is(not(nullValue())));

            assertThat(name, achievements.remove(subject), is(true));
        }

        assertThat("org.bukkit.Achievement has too many achievements", achievements, is(empty()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void verifyStatisticMapping() throws Throwable {
        HashMultiset<Statistic> statistics = HashMultiset.create();
        for (net.minecraft.server.Statistic statistic : (List<net.minecraft.server.Statistic>) StatisticList.stats) {
            if (statistic instanceof net.minecraft.server.Achievement) {
                continue;
            }
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
