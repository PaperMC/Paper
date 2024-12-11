package org.bukkit.entity;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.entity.CraftTropicalFish;
import org.bukkit.entity.TropicalFish.Pattern;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class TropicalFishTest {

    @Test
    public void testVariants() {
        this.testVariant(65536, DyeColor.ORANGE, DyeColor.WHITE, Pattern.KOB);
        this.testVariant(917504, DyeColor.RED, DyeColor.WHITE, Pattern.KOB);
        this.testVariant(918273, DyeColor.RED, DyeColor.WHITE, Pattern.BLOCKFISH);
        this.testVariant(918529, DyeColor.RED, DyeColor.WHITE, Pattern.BETTY);
        this.testVariant(16778497, DyeColor.WHITE, DyeColor.ORANGE, Pattern.CLAYFISH);
        this.testVariant(50660352, DyeColor.LIME, DyeColor.LIGHT_BLUE, Pattern.BRINELY);
        this.testVariant(50726144, DyeColor.PINK, DyeColor.LIGHT_BLUE, Pattern.SPOTTY);
        this.testVariant(50790656, DyeColor.GRAY, DyeColor.LIGHT_BLUE, Pattern.SUNSTREAK);
        this.testVariant(67108865, DyeColor.WHITE, DyeColor.YELLOW, Pattern.FLOPPER);
        this.testVariant(67110144, DyeColor.WHITE, DyeColor.YELLOW, Pattern.SPOTTY);
        this.testVariant(67371265, DyeColor.YELLOW, DyeColor.YELLOW, Pattern.STRIPEY);
        this.testVariant(67764993, DyeColor.PURPLE, DyeColor.YELLOW, Pattern.BLOCKFISH);
        this.testVariant(101253888, DyeColor.CYAN, DyeColor.PINK, Pattern.DASHER);
        this.testVariant(117441025, DyeColor.WHITE, DyeColor.GRAY, Pattern.GLITTER);
        this.testVariant(117441280, DyeColor.WHITE, DyeColor.GRAY, Pattern.DASHER);
        this.testVariant(117441536, DyeColor.WHITE, DyeColor.GRAY, Pattern.BRINELY);
        this.testVariant(117506305, DyeColor.ORANGE, DyeColor.GRAY, Pattern.STRIPEY);
        this.testVariant(117899265, DyeColor.GRAY, DyeColor.GRAY, Pattern.FLOPPER);
        this.testVariant(118161664, DyeColor.BLUE, DyeColor.GRAY, Pattern.SUNSTREAK);
        this.testVariant(134217984, DyeColor.WHITE, DyeColor.LIGHT_GRAY, Pattern.SUNSTREAK);
        this.testVariant(234882305, DyeColor.WHITE, DyeColor.RED, Pattern.CLAYFISH);
        this.testVariant(235340288, DyeColor.GRAY, DyeColor.RED, Pattern.SNOOPER);
    }

    private void testVariant(int variant, DyeColor bodyColor, DyeColor patternColor, Pattern pattern) {
        assertThat(CraftTropicalFish.getData(patternColor, bodyColor, pattern), is(variant), "variant write");
        assertThat(CraftTropicalFish.getPatternColor(variant), is(patternColor), "pattern colour read");
        assertThat(CraftTropicalFish.getBodyColor(variant), is(bodyColor), "body colour read");
        assertThat(CraftTropicalFish.getPattern(variant), is(pattern), "pattern read");
    }
}
