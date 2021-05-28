package io.papermc.paper.effects;

import io.papermc.paper.adventure.PaperAdventure;
import net.minecraft.world.effect.MobEffectCategory;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AllFeatures
public class EffectCategoryTest {

    @Test
    public void testEffectCategoriesExist() {
        for (MobEffectCategory mobEffectInfo : MobEffectCategory.values()) {
            assertNotNull(CraftPotionEffectType.fromNMS(mobEffectInfo), mobEffectInfo + " is missing a bukkit equivalent");
        }
    }

    @Test
    public void testCategoryHasEquivalentColors() {
        for (MobEffectCategory mobEffectInfo : MobEffectCategory.values()) {
            PotionEffectType.Category bukkitEffectCategory = CraftPotionEffectType.fromNMS(mobEffectInfo);
            assertEquals(bukkitEffectCategory.getColor(), PaperAdventure.asAdventure(mobEffectInfo.getTooltipFormatting()), mobEffectInfo.getTooltipFormatting().name() + " doesn't equal " + bukkitEffectCategory.getColor());
        }
    }
}
