package org.bukkit.craftbukkit.potion;

import net.minecraft.world.effect.MobEffectCategory;
import org.bukkit.potion.PotionEffectTypeCategory;

@Deprecated
public final class CraftPotionEffectTypeCategory {

    public static PotionEffectTypeCategory minecraftToBukkit(MobEffectCategory minecraft) {
        return switch (minecraft) {
            case BENEFICIAL -> PotionEffectTypeCategory.BENEFICIAL;
            case HARMFUL -> PotionEffectTypeCategory.HARMFUL;
            case NEUTRAL -> PotionEffectTypeCategory.NEUTRAL;
        };
    }
}
