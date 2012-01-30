package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StatisticTest {
    @Test
    public void getById() {
        for (Statistic statistic : Statistic.values()) {
            assertThat(Statistic.getById(statistic.getId()), is(statistic));
        }
    }
}
