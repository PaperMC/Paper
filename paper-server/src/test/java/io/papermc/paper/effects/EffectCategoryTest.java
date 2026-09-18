package io.papermc.paper.effects;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.world.effect.MobEffectCategory;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Normal
public class EffectCategoryTest {

    @Test
    public void testCategoryHasEquivalentColors() {
        for (MobEffectCategory mobEffectInfo : MobEffectCategory.values()) {
            PotionEffectType.Category bukkitEffectCategory = CraftPotionEffectType.fromVanilla(mobEffectInfo);
            assertEquals(bukkitEffectCategory.getColor(), NamedTextColor.NAMES.value(net.minecraft.network.chat.TextColor.fromLegacyFormat(mobEffectInfo.getTooltipFormatting()).toString()), mobEffectInfo.getTooltipFormatting().name() + " doesn't equal " + bukkitEffectCategory.getColor());
        }
    }
}
