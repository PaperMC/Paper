package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Collections;
import java.util.List;

import net.minecraft.server.AchievementList;

import org.bukkit.craftbukkit.CraftAchievement;
import org.bukkit.support.AbstractTestingBase;
import org.bukkit.support.Util;
import org.junit.Test;

import com.google.common.collect.Lists;

public class AchievementTest extends AbstractTestingBase {
    @Test
    @SuppressWarnings("unchecked")
    public void verifyMapping() throws Throwable {
        List<Achievement> achievements = Lists.newArrayList(Achievement.values());

        for (net.minecraft.server.Achievement statistic : (List<net.minecraft.server.Achievement>) AchievementList.e) {
            String name = statistic.e;

            String message = String.format("org.bukkit.Achievement is missing: '%s'", name);

            Achievement subject = CraftAchievement.getAchievement(name);
            assertNotNull(message, subject);

            assertTrue(name, achievements.remove(subject));
        }

        assertThat("org.bukkit.Achievement has too many achievements", achievements, is(Collections.EMPTY_LIST));
    }
}
