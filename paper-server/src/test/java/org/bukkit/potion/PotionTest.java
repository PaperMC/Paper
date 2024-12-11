package org.bukkit.potion;

import static org.junit.jupiter.api.Assertions.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

@AllFeatures
public class PotionTest {
    @Test
    public void testEffectCompleteness() throws Throwable {
        Map<PotionType, String> effects = new EnumMap(PotionType.class);
        for (Potion reg : BuiltInRegistries.POTION) {
            List<MobEffectInstance> eff = reg.getEffects();
            if (eff.size() != 1) continue;
            PotionEffectType type = CraftPotionEffectType.minecraftHolderToBukkit(eff.get(0).getEffect());
            assertNotNull(type, String.valueOf(reg));

            PotionType enumType = PotionType.getByEffect(type);
            assertNotNull(enumType, type.getName());

            effects.put(enumType, enumType.name());
        }

        assertEquals(PotionType.values().length - /* PotionTypes with no/shared Effects */ (5 + 22 /* There are 22 new strong / long potion types */), effects.entrySet().size());
    }

    @Test
    public void testEffectType() {
        for (MobEffect nms : BuiltInRegistries.MOB_EFFECT) {
            ResourceLocation key = BuiltInRegistries.MOB_EFFECT.getKey(nms);

            PotionEffectType bukkit = CraftPotionEffectType.minecraftToBukkit(nms);

            assertNotNull(bukkit, "No Bukkit type for " + key);
            assertFalse(bukkit.getName().contains("UNKNOWN"), "No name for " + key);

            PotionEffectType byName = FieldRename.getByName_PotionEffectType(bukkit.getName());
            assertEquals(bukkit, byName, "Same type not returned by name " + key);
        }
    }
}
