package org.bukkit.craftbukkit.potion;

import com.google.common.base.Preconditions;
import net.minecraft.world.effect.MobEffectInfo;
import org.bukkit.potion.PotionEffectTypeCategory;

public final class CraftPotionEffectTypeCategory {

    public static PotionEffectTypeCategory minecraftToBukkit(MobEffectInfo minecraft) {
        Preconditions.checkArgument(minecraft != null);
        return PotionEffectTypeCategory.valueOf(minecraft.name());
    }

    public static MobEffectInfo bukkitToMinecraft(PotionEffectTypeCategory bukkit) {
        Preconditions.checkArgument(bukkit != null);
        return MobEffectInfo.valueOf(bukkit.name());
    }
}
