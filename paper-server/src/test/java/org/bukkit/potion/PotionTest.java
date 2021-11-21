package org.bukkit.potion;

import static org.junit.Assert.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectList;
import net.minecraft.world.item.alchemy.PotionRegistry;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class PotionTest extends AbstractTestingBase {
    @Test
    public void testEffectCompleteness() throws Throwable {
        Map<PotionType, String> effects = new EnumMap(PotionType.class);
        for (Object reg : IRegistry.POTION) {
            List<MobEffect> eff = ((PotionRegistry) reg).getEffects();
            if (eff.size() != 1) continue;
            int id = MobEffectList.getId(eff.get(0).getEffect());
            PotionEffectType type = PotionEffectType.getById(id);
            assertNotNull(String.valueOf(id), PotionEffectType.getById(id));

            PotionType enumType = PotionType.getByEffect(type);
            assertNotNull(type.getName(), enumType);

            effects.put(enumType, enumType.name());
        }

        assertEquals(effects.entrySet().size(), PotionType.values().length - /* PotionTypes with no/shared Effects */ 6);
    }

    @Test
    public void testEffectType() {
        for (MobEffectList nms : IRegistry.MOB_EFFECT) {
            MinecraftKey key = IRegistry.MOB_EFFECT.getKey(nms);

            int id = MobEffectList.getId(nms);
            PotionEffectType bukkit = PotionEffectType.getById(id);

            assertNotNull("No Bukkit type for " + key, bukkit);
            assertFalse("No name for " + key, bukkit.getName().contains("UNKNOWN"));

            PotionEffectType byName = PotionEffectType.getByName(bukkit.getName());
            assertEquals("Same type not returned by name " + key, bukkit, byName);
        }
    }
}
