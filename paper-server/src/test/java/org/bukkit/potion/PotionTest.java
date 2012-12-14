package org.bukkit.potion;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.craftbukkit.potion.CraftPotionBrewer;
import org.bukkit.support.Util;
import org.junit.BeforeClass;
import org.junit.Test;

import net.minecraft.server.MobEffectList;

public class PotionTest {

    @BeforeClass
    public static void setUp() {
        Potion.setPotionBrewer(new CraftPotionBrewer());
        MobEffectList.BLINDNESS.getClass();
        PotionEffectType.stopAcceptingRegistrations();
    }

    @Test
    public void getEffects() {
        for (PotionType type : PotionType.values()) {
            for (PotionEffect effect : new Potion(type).getEffects()) {
                PotionEffectType potionType = effect.getType();
                assertThat(effect.getType(), is(sameInstance(PotionEffectType.getById(potionType.getId()))));

                assertNotNull(potionType.getName(), PotionType.getByEffect(potionType));
            }
        }
    }

    @Test
    public void testEffectCompleteness() throws SecurityException, IllegalAccessException, NoSuchFieldException {
        Map<Integer, ?> effectDurations = Util.getInternalState(net.minecraft.server.PotionBrewer.class, null, "effectDurations");

        Map<PotionType, String> effects = new EnumMap(PotionType.class);
        for (int id : effectDurations.keySet()) {
            PotionEffectType type = PotionEffectType.getById(id);
            assertNotNull(String.valueOf(id), PotionEffectType.getById(id));

            PotionType enumType = PotionType.getByEffect(type);
            assertNotNull(type.getName(), enumType);

            assertThat(enumType.name(), effects.put(enumType, enumType.name()), is((String)null));
        }

        assertThat(effects.entrySet(), hasSize(effectDurations.size()));
        assertThat(effectDurations.entrySet(), hasSize(PotionType.values().length - /* WATER */ 1));
    }
}
