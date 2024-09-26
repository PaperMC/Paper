package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import net.minecraft.world.item.EnumColor;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@Normal
public class DyeColorsTest {

    @ParameterizedTest
    @EnumSource(DyeColor.class)
    public void checkColor(DyeColor dye) {
        Color color = dye.getColor();
        int nmsColorArray = EnumColor.byId(dye.getWoolData()).getTextureDiffuseColor();
        Color nmsColor = Color.fromARGB(nmsColorArray);
        assertThat(color, is(nmsColor));
    }

    @ParameterizedTest
    @EnumSource(DyeColor.class)
    public void checkFireworkColor(DyeColor dye) {
        Color color = dye.getFireworkColor();
        int nmsColor = EnumColor.byId(dye.getWoolData()).getFireworkColor();
        assertThat(color, is(Color.fromRGB(nmsColor)));
    }
}
