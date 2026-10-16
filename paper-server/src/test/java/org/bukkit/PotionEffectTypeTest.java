package org.bukkit;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import static org.bukkit.support.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AllFeatures
public class PotionEffectTypeTest {

    @Test
    public void verifyMapping() {
        List<PotionEffectType> effects = Lists.newArrayList(PotionEffectType.values());

        for (Identifier key : BuiltInRegistries.MOB_EFFECT.keySet()) {
            String name = key.getPath();
            PotionEffectType effect = PotionEffectType.getByKey(CraftNamespacedKey.fromMinecraft(key));

            String message = String.format("org.bukkit.PotionEffectType is missing '%s'", name);
            assertNotNull(effect, message);

            effects.remove(effect);
        }

        assertThat(effects, is(Collections.emptyList()), "org.bukkit.PotionEffectType has too many effects");
    }
}
