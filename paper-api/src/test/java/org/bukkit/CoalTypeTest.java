package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CoalTypeTest {
    @Test
    public void getByData() {
        for (CoalType coalType : CoalType.values()) {
            assertThat(CoalType.getByData(coalType.getData()), is(coalType));
        }
    }
}
