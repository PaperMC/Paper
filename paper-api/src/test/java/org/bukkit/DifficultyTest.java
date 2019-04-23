package org.bukkit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class DifficultyTest {
    @Test
    public void getByValue() {
        for (Difficulty difficulty : Difficulty.values()) {
            assertThat(Difficulty.getByValue(difficulty.getValue()), is(difficulty));
        }
    }
}
