package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class AchievementTest {

    @Test
    public void getById() {
        for (Achievement achievement : Achievement.values()) {
            assertThat(Achievement.getById(achievement.getId()), is(achievement));
        }
    }

    @Test
    public void getByOffset() {
        assertThat(Achievement.getById(Achievement.STATISTIC_OFFSET), is(Achievement.values()[0]));
    }
}
