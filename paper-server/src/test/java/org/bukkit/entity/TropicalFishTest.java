package org.bukkit.entity;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.entity.CraftTropicalFish;
import org.bukkit.entity.TropicalFish.Pattern;
import org.junit.jupiter.api.Test;

public class TropicalFishTest {

    @Test
    public void testVariants() {
        testVariant(65536, DyeColor.ORANGE, DyeColor.WHITE, Pattern.KOB);
        testVariant(917504, DyeColor.RED, DyeColor.WHITE, Pattern.KOB);
        testVariant(918273, DyeColor.RED, DyeColor.WHITE, Pattern.BLOCKFISH);
        testVariant(918529, DyeColor.RED, DyeColor.WHITE, Pattern.BETTY);
        testVariant(16778497, DyeColor.WHITE, DyeColor.ORANGE, Pattern.CLAYFISH);
        testVariant(50660352, DyeColor.LIME, DyeColor.LIGHT_BLUE, Pattern.BRINELY);
        testVariant(50726144, DyeColor.PINK, DyeColor.LIGHT_BLUE, Pattern.SPOTTY);
        testVariant(50790656, DyeColor.GRAY, DyeColor.LIGHT_BLUE, Pattern.SUNSTREAK);
        testVariant(67108865, DyeColor.WHITE, DyeColor.YELLOW, Pattern.FLOPPER);
        testVariant(67110144, DyeColor.WHITE, DyeColor.YELLOW, Pattern.SPOTTY);
        testVariant(67371265, DyeColor.YELLOW, DyeColor.YELLOW, Pattern.STRIPEY);
        testVariant(67764993, DyeColor.PURPLE, DyeColor.YELLOW, Pattern.BLOCKFISH);
        testVariant(101253888, DyeColor.CYAN, DyeColor.PINK, Pattern.DASHER);
        testVariant(117441025, DyeColor.WHITE, DyeColor.GRAY, Pattern.GLITTER);
        testVariant(117441280, DyeColor.WHITE, DyeColor.GRAY, Pattern.DASHER);
        testVariant(117441536, DyeColor.WHITE, DyeColor.GRAY, Pattern.BRINELY);
        testVariant(117506305, DyeColor.ORANGE, DyeColor.GRAY, Pattern.STRIPEY);
        testVariant(117899265, DyeColor.GRAY, DyeColor.GRAY, Pattern.FLOPPER);
        testVariant(118161664, DyeColor.BLUE, DyeColor.GRAY, Pattern.SUNSTREAK);
        testVariant(134217984, DyeColor.WHITE, DyeColor.LIGHT_GRAY, Pattern.SUNSTREAK);
        testVariant(234882305, DyeColor.WHITE, DyeColor.RED, Pattern.CLAYFISH);
        testVariant(235340288, DyeColor.GRAY, DyeColor.RED, Pattern.SNOOPER);
    }

    private void testVariant(int variant, DyeColor bodyColor, DyeColor patternColor, Pattern pattern) {
        assertThat(CraftTropicalFish.getData(patternColor, bodyColor, pattern), is(variant), "variant write");
        assertThat(CraftTropicalFish.getPatternColor(variant), is(patternColor), "pattern colour read");
        assertThat(CraftTropicalFish.getBodyColor(variant), is(bodyColor), "body colour read");
        assertThat(CraftTropicalFish.getPattern(variant), is(pattern), "pattern read");
    }
}
