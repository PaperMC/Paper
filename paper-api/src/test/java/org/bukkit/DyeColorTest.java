package org.bukkit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.bukkit.support.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DyeColorTest {

    @ParameterizedTest
    @EnumSource(DyeColor.class)
    public void getByData(DyeColor dye) {
        byte data = dye.getWoolData();

        DyeColor byData = DyeColor.getByWoolData(data);
        assertThat(byData, is(dye));
    }

    @ParameterizedTest
    @EnumSource(DyeColor.class)
    public void getByWoolData(DyeColor dye) {
        byte data = dye.getWoolData();

        DyeColor byData = DyeColor.getByWoolData(data);
        assertThat(byData, is(dye));
    }

    @ParameterizedTest
    @EnumSource(DyeColor.class)
    public void getByDyeData(DyeColor dye) {
        byte data = dye.getDyeData();

        DyeColor byData = DyeColor.getByDyeData(data);
        assertThat(byData, is(dye));
    }
}
