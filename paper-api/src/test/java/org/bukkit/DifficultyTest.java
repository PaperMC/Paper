package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DifficultyTest {
    @Test
    public void getByValue() {
        for (Difficulty difficulty : Difficulty.values()) {
            assertThat(Difficulty.getByValue(difficulty.getValue()), is(difficulty));
        }
    }
}
