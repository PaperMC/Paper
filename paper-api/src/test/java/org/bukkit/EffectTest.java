package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class EffectTest {
    @Test
    public void getById() {
        for (Effect effect : Effect.values()) {
            assertThat(Effect.getById(effect.getId()), is(effect));
        }
    }
}
