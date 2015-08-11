package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.material.Colorable;
import org.bukkit.material.Dye;
import org.bukkit.material.Wool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DyeColorTest {

    @Parameters(name= "{index}: {0}")
    public static List<Object[]> data() {
        List<Object[]> list = new ArrayList<Object[]>();
        for (DyeColor dye : DyeColor.values()) {
            list.add(new Object[] {dye});
        }
        return list;
    }

    @Parameter public DyeColor dye;

    @Test
    @SuppressWarnings("deprecation")
    public void getByData() {
        byte data = dye.getData();

        DyeColor byData = DyeColor.getByData(data);
        assertThat(byData, is(dye));
    }

    @Test
    public void getByWoolData() {
        byte data = dye.getWoolData();

        DyeColor byData = DyeColor.getByWoolData(data);
        assertThat(byData, is(dye));
    }

    @Test
    public void getByDyeData() {
        byte data = dye.getDyeData();

        DyeColor byData = DyeColor.getByDyeData(data);
        assertThat(byData, is(dye));
    }

    @Test
    public void getDyeDyeColor() {
        testColorable(new Dye(Material.INK_SACK, dye.getDyeData()));
        testColorable(new Dye(dye));
    }

    @Test
    public void getWoolDyeColor() {
        testColorable(new Wool(Material.WOOL, dye.getWoolData()));
    }

    private void testColorable(final Colorable colorable) {
        assertThat(colorable.getColor(), is(this.dye));
    }
}
