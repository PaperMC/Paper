package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.jupiter.api.Test;

public class EffectTest {
    @Test
    public void getById() {
        for (Effect effect : Effect.values()) {
            assertThat(Effect.getById(effect.getId()), is(effect));
        }
    }
}
