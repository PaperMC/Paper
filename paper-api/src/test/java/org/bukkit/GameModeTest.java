package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.jupiter.api.Test;

public class GameModeTest {
    @Test
    public void getByValue() {
        for (GameMode gameMode : GameMode.values()) {
            assertThat(GameMode.getByValue(gameMode.getValue()), is(gameMode));
        }
    }
}
