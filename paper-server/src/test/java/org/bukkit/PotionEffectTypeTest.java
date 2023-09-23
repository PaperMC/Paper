package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class PotionEffectTypeTest extends AbstractTestingBase {

    @Test
    public void verifyMapping() {
        List<PotionEffectType> effects = Lists.newArrayList(PotionEffectType.values());

        for (MinecraftKey key : BuiltInRegistries.MOB_EFFECT.keySet()) {
            String name = key.getPath();
            PotionEffectType effect = PotionEffectType.getByKey(CraftNamespacedKey.fromMinecraft(key));

            String message = String.format("org.bukkit.PotionEffectType is missing '%s'", name);
            assertNotNull(effect, message);

            effects.remove(effect);
        }

        assertThat(effects, is(Collections.EMPTY_LIST), "org.bukkit.PotionEffectType has too many effects");
    }
}
