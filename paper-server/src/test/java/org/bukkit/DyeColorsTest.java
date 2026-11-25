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
        net.minecraft.world.item.DyeColor nmsDye = net.minecraft.world.item.DyeColor.byId(dye.getWoolData());
        int nmsColorArray = net.minecraft.util.CommonColors.TEXTURE_TINT_COLORS.pick(nmsDye);
        Color nmsColor = Color.fromARGB(nmsColorArray);
        assertThat(color, is(nmsColor));
    }

    @ParameterizedTest
    @EnumSource(org.bukkit.DyeColor.class)
    public void checkFireworkColor(org.bukkit.DyeColor dye) {
        Color color = dye.getFireworkColor();
        net.minecraft.world.item.DyeColor nmsDye = net.minecraft.world.item.DyeColor.byId(dye.getWoolData());
        int nmsColor = net.minecraft.world.item.component.FireworkExplosion.defaultFireworkTint(nmsDye);
        assertThat(color, is(Color.fromRGB(nmsColor)));
    }
}
