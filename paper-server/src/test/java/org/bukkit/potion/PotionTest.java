package org.bukkit.potion;

import static org.junit.Assert.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.server.IRegistry;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.PotionRegistry;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class PotionTest extends AbstractTestingBase {
    @Test
    public void testEffectCompleteness() throws Throwable {
        Map<PotionType, String> effects = new EnumMap(PotionType.class);
        for (Object reg : IRegistry.POTION) {
            List<MobEffect> eff = ((PotionRegistry)reg).a();
            if (eff.size() != 1) continue;
            int id = MobEffectList.getId(eff.get(0).getMobEffect());
            PotionEffectType type = PotionEffectType.getById(id);
            assertNotNull(String.valueOf(id), PotionEffectType.getById(id));

            PotionType enumType = PotionType.getByEffect(type);
            assertNotNull(type.getName(), enumType);

            effects.put(enumType, enumType.name());
        }

        assertEquals(effects.entrySet().size(), PotionType.values().length - /* PotionTypes with no/shared Effects */ 6);
    }
}
