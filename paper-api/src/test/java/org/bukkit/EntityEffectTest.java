package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class EntityEffectTest {
    @Test
    public void getByData() {
        for (EntityEffect entityEffect : EntityEffect.values()) {
            assertThat(EntityEffect.getByData(entityEffect.getData()), is(entityEffect));
        }
    }
}
