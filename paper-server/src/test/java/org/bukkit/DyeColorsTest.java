package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.bukkit.support.environment.Normal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@Normal
public class DyeColorsTest {

    @ParameterizedTest
    @EnumSource(DyeColor.class)
    public void checkColor(DyeColor dye) {
        Color color = dye.getColor();
        int nmsColorArray = net.minecraft.world.item.DyeColor.byId(dye.getWoolData()).getTextureDiffuseColor(); // Paper - remap fix
        Color nmsColor = Color.fromARGB(nmsColorArray);
        assertThat(color, is(nmsColor));
    }

    @ParameterizedTest
    @EnumSource(org.bukkit.DyeColor.class)
    public void checkFireworkColor(org.bukkit.DyeColor dye) {
        Color color = dye.getFireworkColor();
        int nmsColor = net.minecraft.world.item.DyeColor.byId(dye.getWoolData()).getFireworkColor(); // Paper - remap fix
        assertThat(color, is(Color.fromRGB(nmsColor)));
    }
}
