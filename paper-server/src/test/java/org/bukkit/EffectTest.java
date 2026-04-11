package org.bukkit;

import com.google.common.base.Joiner;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.level.block.LevelEvent;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.bukkit.support.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

@Normal
public class EffectTest {

    private static boolean isNotDeprecated(Effect effect) throws ReflectiveOperationException {
        return !Effect.class.getDeclaredField(effect.name()).isAnnotationPresent(Deprecated.class);
    }

    private static final Int2ObjectArrayMap<Effect> ID_TO_EFFECT = new Int2ObjectArrayMap<>(); // api
    private static final IntSet LEVEL_EVENTS = new IntArraySet(); // internal

    @BeforeAll
    public static void init() throws ReflectiveOperationException {
        for (final Effect effect : Effect.values()) {
            if (isNotDeprecated(effect)) {
                final Effect put = ID_TO_EFFECT.put(effect.getId(), effect);
                assertNull(put, "Duplicate API effect: " + put);
            }
        }

        for (final Field field : LevelEvent.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.getType() == int.class) {
                LEVEL_EVENTS.add((int) field.get(null));
            }
        }
    }

    @Test
    public void checkAllApiExists() {
        final IntSet missingEvents = new IntArraySet();
        for (final int event : LEVEL_EVENTS) {
            if (!ID_TO_EFFECT.containsKey(event)) {
                missingEvents.add(event);
            }
        }
        if (!missingEvents.isEmpty()) {
            fail("Missing API Effects:\n" + Joiner.on("\n").join(missingEvents));
        }
    }

    @Test
    public void checkNoExtraApi() {
        final Set<Effect> extraApiEffects = new HashSet<>();
        ID_TO_EFFECT.forEach((key, value) -> {
            if (!LEVEL_EVENTS.contains(key)) {
                extraApiEffects.add(value);
            }
        });
        if (!extraApiEffects.isEmpty()) {
            fail("Extra API Effects:\n" + Joiner.on("\n").join(extraApiEffects));
        }
    }

    @Test
    public void testByIdRoundTrip() {
        ID_TO_EFFECT.forEach((key, value) -> {
            assertThat(Effect.getById(key), is(value));
        });
    }
}
