package org.bukkit.potion;

import static org.junit.jupiter.api.Assertions.*;
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
import org.junit.jupiter.api.Test;

public class PotionTest extends AbstractTestingBase {
    @Test
    public void testEffectCompleteness() throws Throwable {
        Map<PotionType, String> effects = new EnumMap(PotionType.class);
        for (PotionRegistry reg : BuiltInRegistries.POTION) {
            List<MobEffect> eff = reg.getEffects();
            if (eff.size() != 1) continue;
            PotionEffectType type = CraftPotionEffectType.minecraftToBukkit(eff.get(0).getEffect());
            assertNotNull(type, String.valueOf(reg));

            PotionType enumType = PotionType.getByEffect(type);
            assertNotNull(enumType, type.getName());

            effects.put(enumType, enumType.name());
        }

        assertEquals(effects.entrySet().size(), PotionType.values().length - /* PotionTypes with no/shared Effects */ (6 + 22 /* There are 22 new strong / long potion types */));
    }

    @Test
    public void testEffectType() {
        for (MobEffectList nms : BuiltInRegistries.MOB_EFFECT) {
            MinecraftKey key = BuiltInRegistries.MOB_EFFECT.getKey(nms);

            PotionEffectType bukkit = CraftPotionEffectType.minecraftToBukkit(nms);

            assertNotNull(bukkit, "No Bukkit type for " + key);
            assertFalse(bukkit.getName().contains("UNKNOWN"), "No name for " + key);

            PotionEffectType byName = PotionEffectType.getByName(bukkit.getName());
            assertEquals(bukkit, byName, "Same type not returned by name " + key);
        }
    }
}
