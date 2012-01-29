package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DyeColorTest {
    @Test
    public void getByData() {
        for (DyeColor dyeColor : DyeColor.values()) {
            assertThat(DyeColor.getByData(dyeColor.getData()), is(dyeColor));
        }
    }
}
