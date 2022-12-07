package org.bukkit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class PotionEffectTypeTest extends AbstractTestingBase {

    @Test
    public void verifyMapping() {
        List<PotionEffectType> effects = Lists.newArrayList(PotionEffectType.values());

        for (MinecraftKey key : BuiltInRegistries.MOB_EFFECT.keySet()) {
            String name = key.getPath();
            PotionEffectType effect = PotionEffectType.getByKey(CraftNamespacedKey.fromMinecraft(key));

            String message = String.format("org.bukkit.PotionEffectType is missing '%s'", name);
            assertNotNull(message, effect);

            effects.remove(effect);
        }

        assertThat("org.bukkit.PotionEffectType has too many effects", effects, is(Collections.EMPTY_LIST));
    }
}
