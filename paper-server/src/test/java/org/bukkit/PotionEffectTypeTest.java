package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import org.bukkit.craftbukkit.potion.CraftPotionEffectTypeCategory;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

@AllFeatures
public class PotionEffectTypeTest {

    @Test
    public void verifyMapping() {
        List<PotionEffectType> effects = Lists.newArrayList(PotionEffectType.values());

        for (ResourceLocation key : BuiltInRegistries.MOB_EFFECT.keySet()) {
            String name = key.getPath();
            PotionEffectType effect = PotionEffectType.getByKey(CraftNamespacedKey.fromMinecraft(key));

            String message = String.format("org.bukkit.PotionEffectType is missing '%s'", name);
            assertNotNull(effect, message);

            effects.remove(effect);
        }

        assertThat(effects, is(Collections.emptyList()), "org.bukkit.PotionEffectType has too many effects");
    }

    @Test
    public void verifyCategories() {
        for (PotionEffectTypeCategory category : PotionEffectTypeCategory.values()) {
            String categoryName = category.name();
            assertDoesNotThrow(() -> CraftPotionEffectTypeCategory.bukkitToMinecraft(category), "PotionEffectTypeCategory." + categoryName + " exists but MobEffectInfo." + categoryName + " does not!");
        }

        for (MobEffectCategory info : MobEffectCategory.values()) {
            assertDoesNotThrow(() -> CraftPotionEffectTypeCategory.minecraftToBukkit(info), "Missing PotionEffectTypeCategory for MobEffectInfo." + info.name());
        }
    }
}
