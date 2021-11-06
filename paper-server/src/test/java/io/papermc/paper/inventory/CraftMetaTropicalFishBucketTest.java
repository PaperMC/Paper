package io.papermc.paper.inventory;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@AllFeatures
public class CraftMetaTropicalFishBucketTest {

    @Test
    public void testAllCombinations() {
        final var rawMeta = new ItemStack(Material.TROPICAL_FISH_BUCKET).getItemMeta();
        Assertions.assertTrue(rawMeta instanceof TropicalFishBucketMeta, "Meta was not a tropical fish bucket");

        final var meta = (TropicalFishBucketMeta) rawMeta;

        for (final var bodyColor : DyeColor.values()) {
            for (final var pattern : TropicalFish.Pattern.values()) {
                for (final var patternColor : DyeColor.values()) {
                    meta.setBodyColor(bodyColor);
                    Assertions.assertEquals(bodyColor, meta.getBodyColor(), "Body color did not match post body color!");

                    meta.setPattern(pattern);
                    Assertions.assertEquals(pattern, meta.getPattern(), "Pattern did not match post pattern!");
                    Assertions.assertEquals(bodyColor, meta.getBodyColor(), "Body color did not match post pattern!");

                    meta.setPatternColor(patternColor);
                    Assertions.assertEquals(pattern, meta.getPattern(), "Pattern did not match post pattern color!");
                    Assertions.assertEquals(bodyColor, meta.getBodyColor(), "Body color did not match post pattern color!");
                    Assertions.assertEquals(patternColor, meta.getPatternColor(), "Pattern color did not match post pattern color!");
                }
            }
        }
    }

}
