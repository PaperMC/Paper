package org.bukkit.potion;

import static org.junit.Assert.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectList;
import net.minecraft.world.item.alchemy.PotionRegistry;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class PotionTest extends AbstractTestingBase {
    @Test
    public void testEffectCompleteness() throws Throwable {
        Map<PotionType, String> effects = new EnumMap(PotionType.class);
        for (PotionRegistry reg : BuiltInRegistries.POTION) {
            List<MobEffect> eff = reg.getEffects();
            if (eff.size() != 1) continue;
            PotionEffectType type = CraftPotionEffectType.minecraftToBukkit(eff.get(0).getEffect());
            assertNotNull(String.valueOf(reg), type);

            PotionType enumType = PotionType.getByEffect(type);
            assertNotNull(type.getName(), enumType);

            effects.put(enumType, enumType.name());
        }

        assertEquals(effects.entrySet().size(), PotionType.values().length - /* PotionTypes with no/shared Effects */ 6);
    }

    @Test
    public void testEffectType() {
        for (MobEffectList nms : BuiltInRegistries.MOB_EFFECT) {
            MinecraftKey key = BuiltInRegistries.MOB_EFFECT.getKey(nms);

            PotionEffectType bukkit = CraftPotionEffectType.minecraftToBukkit(nms);

            assertNotNull("No Bukkit type for " + key, bukkit);
            assertFalse("No name for " + key, bukkit.getName().contains("UNKNOWN"));

            PotionEffectType byName = PotionEffectType.getByName(bukkit.getName());
            assertEquals("Same type not returned by name " + key, bukkit, byName);
        }
    }
}
